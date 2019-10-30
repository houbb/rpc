package com.github.houbb.rpc.client.proxy.impl;

import com.github.houbb.heaven.util.guava.Guavas;
import com.github.houbb.heaven.util.id.impl.Ids;
import com.github.houbb.heaven.util.time.impl.Times;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.client.proxy.RemoteInvokeService;
import com.github.houbb.rpc.client.proxy.ServiceContext;
import com.github.houbb.rpc.common.exception.GenericException;
import com.github.houbb.rpc.common.exception.ShutdownException;
import com.github.houbb.rpc.common.rpc.domain.impl.DefaultRpcRequest;
import com.github.houbb.rpc.common.support.generic.GenericService;
import com.github.houbb.rpc.common.support.status.enums.StatusEnum;

import java.util.List;

/**
 * 泛化调用
 * @author binbin.hou
 * @since 0.1.2
 */
public class GenericReferenceProxy implements GenericService {

    private static final Log LOG = LogFactory.getLog(GenericReferenceProxy.class);

    /**
     * 代理上下文
     * （1）这个信息不应该被修改，应该和指定的 service 紧密关联。
     * @since 0.1.3
     */
    private final ServiceContext proxyContext;

    /**
     * 远程调用接口
     * @since 0.1.3
     */
    private final RemoteInvokeService remoteInvokeService;

    public GenericReferenceProxy(ServiceContext proxyContext, RemoteInvokeService remoteInvokeService) {
        this.proxyContext = proxyContext;
        this.remoteInvokeService = remoteInvokeService;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object $invoke(String method, String[] parameterTypes, Object[] args) throws GenericException {
        // 状态判断
        final int statusCode = proxyContext.statusManager().status();
        if(StatusEnum.ENABLE.code() != statusCode) {
            LOG.error("[Client] current status is: {} , not enable to send request", statusCode);
            throw new ShutdownException("Status is not enable to send request, may be during shutdown.");
        }

        // 构建基本调用参数
        final long createTime = Times.systemTime();
        Object[] actualArgs = new Object[]{method, parameterTypes, args};
        DefaultRpcRequest rpcRequest = new DefaultRpcRequest();
        rpcRequest.serviceId(proxyContext.serviceId());
        rpcRequest.createTime(createTime);
        rpcRequest.paramValues(actualArgs);
        List<String> paramTypeNames = Guavas.newArrayList();
        paramTypeNames.add("java.lang.String");
        paramTypeNames.add("[Ljava.lang.String;");
        paramTypeNames.add("[Ljava.lang.Object;");
        rpcRequest.paramTypeNames(paramTypeNames);
        rpcRequest.methodName("$invoke");
        rpcRequest.returnType(Object.class);

        //proxyContext 中应该是属于当前 service 的对应信息。
        // 每一次调用，对应的 invoke 信息应该是不通的，需要创建新的对象去传递信息
        // rpcRequest 因为要涉及到网络间传输，尽可能保证其简洁性。
        DefaultRemoteInvokeContext context = new DefaultRemoteInvokeContext();
        context.request(rpcRequest);
        context.traceId(Ids.uuid32());
        context.retryTimes(2);
        context.serviceProxyContext(proxyContext);
        context.remoteInvokeService(remoteInvokeService);

        //3. 执行远程调用
        return remoteInvokeService.remoteInvoke(context);
    }

}
