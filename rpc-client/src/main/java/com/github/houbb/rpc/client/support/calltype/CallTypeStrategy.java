package com.github.houbb.rpc.client.support.calltype;

import com.github.houbb.rpc.client.proxy.ProxyContext;
import com.github.houbb.rpc.common.rpc.domain.RpcRequest;

/**
 * 调用方式上下文
 * @author binbin.hou
 * @since 0.1.0
 */
public interface CallTypeStrategy {

    /**
     * 获取结果
     * @param proxyContext 代理上下文
     * @param rpcRequest 请求信息
     * @return 结果
     * @since 0.1.0
     */
    Object result(final ProxyContext proxyContext,
                  final RpcRequest rpcRequest);

}
