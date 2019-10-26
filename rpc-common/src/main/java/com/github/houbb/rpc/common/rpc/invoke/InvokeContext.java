/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.common.rpc.invoke;

import com.github.houbb.rpc.common.rpc.domain.RpcChannelFuture;

import java.util.List;

/**
 * <p> 调用上下文 </p>
 *
 * <pre> Created: 2019/10/26 9:30 上午  </pre>
 * <pre> Project: rpc  </pre>
 *
 * 核心目的：
 * （1）用于定义 filter 相关信息
 * （2）用于 load-balance 相关信息处理
 * @author houbinbin
 * @since 0.0.9
 */
public interface InvokeContext {

    /**
     * 调用唯一标识
     * @return 调用唯一标识
     * @since 0.0.9
     */
    String seqId();

    /**
     * 此次调用的超时时间
     * @return 超时时间
     * @since 0.0.9
     */
    long timeout();

    /**
     * 对应的 channel future 列表信息
     * @return 列表信息
     * @since 0.0.9
     */
    List<RpcChannelFuture> channelFutures();

}
