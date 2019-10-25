/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.common.rpc.domain.impl;

import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.rpc.common.exception.RpcRuntimeException;
import com.github.houbb.rpc.common.rpc.domain.RpcResponse;

/**
 * 默认 rpc 响应结果
 * @author binbin.hou
 * @since 0.0.8
 */
public final class RpcResponses {

    /**
     * 获取结果
     * @param rpcResponse 响应
     * @param tClass 类型
     * @param <T> 泛型
     * @return 结果
     * 如果有异常，则直接抛出异常信息。
     * @since 0.0.8
     */
    @SuppressWarnings("unchecked")
    public static <T> T getResult(final RpcResponse rpcResponse,
                                  final Class<T> tClass) {
        Object result = getResult(rpcResponse);
        return (T) result;
    }

    /**
     * 获取结果
     * @param rpcResponse 响应
     * @return 结果
     * 如果有异常，则直接抛出异常信息。
     * @since 0.0.8
     */
    public static Object getResult(final RpcResponse rpcResponse) {
        if(ObjectUtil.isNull(rpcResponse)) {
            return null;
        }

        Throwable throwable = rpcResponse.error();
        if(ObjectUtil.isNotNull(throwable)) {
            throw new RpcRuntimeException(throwable);
        }
        return rpcResponse.result();
    }

}
