package com.github.houbb.rpc.client.support.calltype.impl;

import com.github.houbb.heaven.annotation.ThreadSafe;
import com.github.houbb.rpc.client.proxy.ProxyContext;
import com.github.houbb.rpc.client.support.calltype.CallTypeStrategy;
import com.github.houbb.rpc.common.rpc.domain.RpcRequest;
import com.github.houbb.rpc.common.rpc.domain.RpcResponse;
import com.github.houbb.rpc.common.rpc.domain.impl.RpcResponses;

/**
 * 同步调用服务实现类
 * @author binbin.hou
 * @since 0.1.0
 */
@ThreadSafe
class SyncCallTypeStrategy implements CallTypeStrategy {

    /**
     * 实例
     * @since 0.1.0
     */
    private static final CallTypeStrategy INSTANCE = new SyncCallTypeStrategy();

    /**
     * 获取实例
     * @since 0.1.0
     */
    public static CallTypeStrategy getInstance(){
        return INSTANCE;
    }

    @Override
    public Object result(ProxyContext proxyContext, RpcRequest rpcRequest) {
        final String seqId = rpcRequest.seqId();
        RpcResponse rpcResponse = proxyContext.invokeService().getResponse(seqId);
        return RpcResponses.getResult(rpcResponse);
    }

}
