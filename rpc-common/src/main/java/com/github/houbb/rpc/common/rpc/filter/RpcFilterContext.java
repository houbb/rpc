/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.common.rpc.filter;

import com.github.houbb.rpc.common.rpc.domain.BaseRpc;
import com.github.houbb.rpc.common.rpc.domain.RpcChannelFuture;
import io.netty.channel.Channel;

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
public interface RpcFilterContext {

    /**
     * 请求信息
     * @see com.github.houbb.rpc.common.rpc.domain.RpcRequest client 入参请求信息
     * @return 请求信息
     * @since 0.0.9
     */
    BaseRpc request();

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

    /**
     * 设置 channel future
     * （1）可以通过 load balance
     * （2）其他各种方式
     * @param channelFuture 消息
     * @return this
     * @since 0.0.9
     */
    RpcFilterContext channelFuture(final RpcChannelFuture channelFuture);

    /**
     *
     * 获取 channel future
     * （1）如果不设置，则默认取 {@link #channelFutures()}第一个
     * （2）如果对应信息为空，则直接报错 {@link com.github.houbb.rpc.common.exception.RpcRuntimeException}
     * @return channel 信息
     * @since 0.0.9
     */
    Channel channel();

}
