package com.github.houbb.rpc.client.support.fail.impl;

import com.github.houbb.heaven.annotation.ThreadSafe;
import com.github.houbb.rpc.client.support.fail.FailStrategy;
import com.github.houbb.rpc.common.rpc.domain.RpcResponse;
import com.github.houbb.rpc.common.rpc.domain.impl.RpcResponses;

/**
 * 快速失败策略
 * @author binbin.hou
 * @since 0.1.1
 */
@ThreadSafe
class FailFastStrategy implements FailStrategy {

    @Override
    public Object fail(RpcResponse rpcResponse) {
        return RpcResponses.getResult(rpcResponse);
    }

}
