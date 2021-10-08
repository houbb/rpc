package com.github.houbb.rpc.client.support.filter.balance;

import com.github.houbb.rpc.client.support.filter.RpcFilter;

/**
 * rpc 过滤器
 * @since 0.2.0
 */
public final class RpcFilters {

    private RpcFilters(){}

    /**
     * 什么都不做的 filter
     * @return filter
     * @since 0.2.0
     */
    public static RpcFilter none() {
        return new NoneRpcFilter();
    }

}
