package com.github.houbb.rpc.common.rpc.domain.impl;

import com.github.houbb.rpc.common.exception.RpcTimeoutException;
import com.github.houbb.rpc.common.rpc.domain.RpcResponse;

/**
 * 响应工厂类
 * @author binbin.hou
 * @since 0.0.7
 */
public final class RpcResponseFactory {

    private RpcResponseFactory(){}

    /**
     * 超时异常信息
     * @since 0.0.7
     */
    private static final DefaultRpcResponse TIMEOUT;

    static {
        TIMEOUT = new DefaultRpcResponse();
        TIMEOUT.error(new RpcTimeoutException());
    }

    /**
     * 获取超时响应结果
     * @return 响应结果
     * @since 0.0.7
     */
    public static RpcResponse timeout() {
        return TIMEOUT;
    }

}
