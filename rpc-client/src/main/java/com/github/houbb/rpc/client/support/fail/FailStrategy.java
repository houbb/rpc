package com.github.houbb.rpc.client.support.fail;

import com.github.houbb.rpc.common.rpc.domain.RpcResponse;

/**
 * 失败策略
 * @author binbin.hou
 * @since 0.1.1
 */
public interface FailStrategy {

    /**
     * 失败策略
     * @param rpcResponse 响应结果
     * @return 最终的结果值
     * @since 0.1.1
     */
    Object fail(final RpcResponse rpcResponse);

}
