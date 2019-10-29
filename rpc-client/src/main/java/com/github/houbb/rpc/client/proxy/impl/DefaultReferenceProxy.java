package com.github.houbb.rpc.client.proxy.impl;

import com.github.houbb.heaven.util.id.impl.Ids;
import com.github.houbb.heaven.util.lang.reflect.ReflectMethodUtil;
import com.github.houbb.heaven.util.time.impl.Times;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.client.constant.enums.CallTypeEnum;
import com.github.houbb.rpc.client.filter.balance.RandomBalanceFilter;
import com.github.houbb.rpc.client.invoke.InvokeService;
import com.github.houbb.rpc.client.proxy.ProxyContext;
import com.github.houbb.rpc.client.proxy.ReferenceProxy;
import com.github.houbb.rpc.common.rpc.domain.RpcRequest;
import com.github.houbb.rpc.common.rpc.domain.RpcResponse;
import com.github.houbb.rpc.common.rpc.domain.impl.DefaultRpcRequest;
import com.github.houbb.rpc.common.rpc.domain.impl.RpcResponses;
import com.github.houbb.rpc.common.rpc.filter.RpcFilter;
import com.github.houbb.rpc.common.rpc.filter.RpcFilterContext;
import com.github.houbb.rpc.common.rpc.filter.impl.DefaultRpcFilterContext;
import io.netty.channel.Channel;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 参考：https://blog.csdn.net/u012240455/article/details/79210250
 *
 * （1）方法执行并不需要一定要有实现类。
 * （2）直接根据反射即可处理相关信息。
 * （3）rpc 是一种强制根据接口进行编程的实现方式。
 * @author binbin.hou
 * @since 0.0.6
 */
public class DefaultReferenceProxy<T> implements ReferenceProxy<T> {

    private static final Log LOG = LogFactory.getLog(DefaultReferenceProxy.class);

    /**
     * 服务标识
     * @since 0.0.6
     */
    private final ProxyContext<T> proxyContext;

    public DefaultReferenceProxy(ProxyContext<T> proxyContext) {
        this.proxyContext = proxyContext;
    }

    /**
     * 反射调用
     * @param proxy 代理
     * @param method 方法
     * @param args 参数
     * @return 结果
     * @throws Throwable 异常
     * @since 0.0.6
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 反射信息处理成为 rpcRequest
        // 避免 null 值返回时，基本类型异常。
        final String seqId = Ids.uuid32();
        final long createTime = Times.systemTime();
        DefaultRpcRequest rpcRequest = new DefaultRpcRequest();
        rpcRequest.serviceId(proxyContext.serviceId());
        rpcRequest.seqId(seqId);
        rpcRequest.createTime(createTime);
        rpcRequest.paramValues(args);
        rpcRequest.paramTypeNames(ReflectMethodUtil.getParamTypeNames(method));
        rpcRequest.methodName(method.getName());
        rpcRequest.callType(proxyContext.callType().code());
        rpcRequest.returnType(method.getReturnType());

        // 这里使用 load-balance 进行选择 channel 写入。
        // 构建 filter 相关信息,结合 pipeline 进行整合
        final RpcFilterContext rpcFilterContext = buildRpcFilterContext(rpcRequest);
        this.doFilter(rpcFilterContext);
        final Channel channel = rpcFilterContext.channel();
        LOG.info("[Client] start call channel id: {}", channel.id().asLongText());

        // 对于信息的写入，实际上有着严格的要求。
        // writeAndFlush 实际是一个异步的操作，直接使用 sync() 可以看到异常信息。
        // 支持的必须是 ByteBuf
        channel.writeAndFlush(rpcRequest).syncUninterruptibly();
        LOG.info("[Client] start call remote with request: {}", rpcRequest);
        final InvokeService invokeService = proxyContext.invokeService();
        invokeService.addRequest(seqId, proxyContext.timeout());

        // 获取结果
        // 如果是 oneWay，则直接设置结果为 null
        if(CallTypeEnum.ONE_WAY.equals(proxyContext.callType())) {
            // 结果可以不是简单的 null，而是根据 result 类型处理，避免基本类型NPE。
            RpcResponse rpcResponse = RpcResponses.result(null, rpcRequest.returnType());
            LOG.info("[Client] call type is one way, set response to {}", rpcResponse);
            invokeService.addResponse(seqId, rpcResponse);
        }
        RpcResponse rpcResponse = invokeService.getResponse(seqId);
        return RpcResponses.getResult(rpcResponse);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T proxy() {
        final Class<T> interfaceClass = proxyContext.serviceInterface();
        ClassLoader classLoader = interfaceClass.getClassLoader();
        Class<?>[] interfaces = new Class[]{interfaceClass};
        return (T) Proxy.newProxyInstance(classLoader, interfaces, this);
    }

    /**
     * 执行过滤
     * @param context 上下文
     * @since 0.0.9
     */
    private void doFilter(final RpcFilterContext context) {
        RpcFilter rpcFilter = new RandomBalanceFilter();
        rpcFilter.filter(context);
    }

    /**
     * 构建 rpc 过滤上下文
     * @return 上下文信息
     * @since 0.0.9
     */
    private RpcFilterContext buildRpcFilterContext(final RpcRequest rpcRequest) {
        DefaultRpcFilterContext context = new DefaultRpcFilterContext();
        context.request(rpcRequest);
        context.timeout(proxyContext.timeout());
        context.channelFutures(proxyContext.channelFutures());
        return context;
    }

}
