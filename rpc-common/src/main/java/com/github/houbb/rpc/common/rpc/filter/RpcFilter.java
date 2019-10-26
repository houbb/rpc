/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.common.rpc.filter;

/**
 * <p> 调用上下文 </p>
 *
 * <pre> Created: 2019/10/26 9:30 上午  </pre>
 * <pre> Project: rpc  </pre>
 *
 * 核心目的：
 * （1）用于定义 filter 相关信息
 * （2）用于 load-balance 相关信息处理
 * （3）后期的路由-分区 都可以视为这样的一个抽象实现而已。
 * @author houbinbin
 * @since 0.0.9
 */
public interface RpcFilter {

    /**
     * 调用
     * @param rpcFilterContext 调用上下文
     * @since 0.0.9
     */
    void filter(final RpcFilterContext rpcFilterContext);

}
