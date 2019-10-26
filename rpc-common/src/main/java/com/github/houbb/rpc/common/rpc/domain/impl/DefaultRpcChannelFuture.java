/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.common.rpc.domain.impl;

import com.github.houbb.rpc.common.config.component.RpcAddress;
import com.github.houbb.rpc.common.rpc.domain.RpcChannelFuture;

import io.netty.channel.ChannelFuture;

/**
 * <p> 默认的实现 </p>
 *
 * <pre> Created: 2019/10/26 9:42 上午  </pre>
 * <pre> Project: rpc  </pre>
 *
 * @author houbinbin
 * @since 0.0.9
 */
public class DefaultRpcChannelFuture implements RpcChannelFuture {

    /**
     * channel future 信息
     * @since 0.0.9
     */
    private ChannelFuture channelFuture;

    /**
     * 对应的地址信息
     * @since 0.0.9
     */
    private RpcAddress address;

    /**
     * 权重信息
     * @since 0.0.9
     */
    private int weight;

    public static DefaultRpcChannelFuture newInstance() {
        return new DefaultRpcChannelFuture();
    }

    @Override
    public ChannelFuture channelFuture() {
        return channelFuture;
    }

    public DefaultRpcChannelFuture channelFuture(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
        return this;
    }

    @Override
    public RpcAddress address() {
        return address;
    }

    public DefaultRpcChannelFuture address(RpcAddress address) {
        this.address = address;
        return this;
    }

    @Override
    public int weight() {
        return weight;
    }

    public DefaultRpcChannelFuture weight(int weight) {
        this.weight = weight;
        return this;
    }

    @Override
    public String toString() {
        return "DefaultRpcChannelFuture{" +
                "channelFuture=" + channelFuture +
                ", address=" + address +
                ", weight=" + weight +
                '}';
    }
}
