package com.github.houbb.rpc.client.proxy;

import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.heaven.util.lang.reflect.ReflectMethodUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.client.proxy.context.ProxyContext;
import com.github.houbb.rpc.common.rpc.domain.RpcResponse;
import com.github.houbb.rpc.common.rpc.domain.impl.DefaultRpcRequest;
import com.github.houbb.rpc.common.support.id.impl.Uuid;
import com.github.houbb.rpc.common.support.time.impl.DefaultSystemTime;
import io.netty.channel.Channel;

import java.lang.reflect.InvocationHandler;
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
public class ReferenceProxy<T> implements InvocationHandler {

    private static final Log LOG = LogFactory.getLog(ReferenceProxy.class);

    /**
     * 服务标识
     * @since 0.0.6
     */
    private final ProxyContext<T> proxyContext;

    /**
     * 暂时私有化该构造器
     * @param proxyContext 代理上下文
     * @since 0.0.6
     */
    private ReferenceProxy(ProxyContext<T> proxyContext) {
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
     * @see Method#getGenericSignature() 通用标识，可以根据这个来优化代码。
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 反射信息处理成为 rpcRequest
        final String seqId = Uuid.getInstance().id();
        final long createTime = DefaultSystemTime.getInstance().time();
        DefaultRpcRequest rpcRequest = new DefaultRpcRequest();
        rpcRequest.serviceId(proxyContext.serviceId());
        rpcRequest.seqId(seqId);
        rpcRequest.createTime(createTime);
        rpcRequest.paramValues(args);
        rpcRequest.paramTypeNames(ReflectMethodUtil.getParamTypeNames(method));
        rpcRequest.methodName(method.getName());

        // 调用远程
        LOG.info("[Client] start call remote with request: {}", rpcRequest);
        proxyContext.invokeService().addRequest(seqId);

        // 这里使用 load-balance 进行选择 channel 写入。
        final Channel channel = getChannel();
        LOG.info("[Client] start call channel id: {}", channel.id().asLongText());

        // 对于信息的写入，实际上有着严格的要求。
        // writeAndFlush 实际是一个异步的操作，直接使用 sync() 可以看到异常信息。
        // 支持的必须是 ByteBuf
        channel.writeAndFlush(rpcRequest).sync();

        // 循环获取结果
        // 通过 Loop+match  wait/notifyAll 来获取
        // 分布式根据 redis+queue+loop
        LOG.info("[Client] start get resp for seqId: {}", seqId);
        RpcResponse rpcResponse = proxyContext.invokeService().getResponse(seqId);
        LOG.info("[Client] start get resp for seqId: {}", seqId);
        Throwable error = rpcResponse.error();
        if(ObjectUtil.isNotNull(error)) {
            throw error;
        }
        return rpcResponse.result();
    }

    /**
     * 获取对应的 channel
     * （1）暂时使用写死的第一个
     * （2）后期这里需要调整，ChannelFuture 加上权重信息。
     * @return 对应的 channel 信息。
     * @since 0.0.6
     */
    private Channel getChannel() {
        return proxyContext.channelFutures().get(0).channel();
    }

    /**
     * 获取代理实例
     * （1）接口只是为了代理。
     * （2）实际调用中更加关心 的是 serviceId
     * @param proxyContext 代理上下文
     * @param <T> 泛型
     * @return 代理实例
     * @since 0.0.6
     */
    @SuppressWarnings("unchecked")
    public static <T> T newProxyInstance(ProxyContext<T> proxyContext) {
        final Class<T> interfaceClass = proxyContext.serviceInterface();
        ClassLoader classLoader = interfaceClass.getClassLoader();
        Class<?>[] interfaces = new Class[]{interfaceClass};
        ReferenceProxy proxy = new ReferenceProxy(proxyContext);
        return (T) Proxy.newProxyInstance(classLoader, interfaces, proxy);
    }

}
