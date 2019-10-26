/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.common.rpc.invoke.impl;

import com.github.houbb.rpc.common.rpc.domain.RpcChannelFuture;
import com.github.houbb.rpc.common.rpc.invoke.InvokeContext;

import java.util.List;

/**
 * <p> 默认的调用上下文 </p>
 *
 * <pre> Created: 2019/10/26 10:00 上午  </pre>
 * <pre> Project: rpc  </pre>
 *
 * @author houbinbin
 * @since 0.0.9
 */
public class DefaultInvokeContext implements InvokeContext {

    /**
     * 调用唯一标识
     * @since 0.0.9
     */
    private String seqId;

    /**
     * 此次调用的超时时间
     * @since 0.0.9
     */
    private long timeout;

    /**
     * 对应的 channel future 列表信息
     * @since 0.0.9
     */
    private List<RpcChannelFuture> channelFutures;

    /**
     * 新创建一个实例
     * @return 实例
     * @since 0.0.9
     */
    public static DefaultInvokeContext newInstance() {
        return new DefaultInvokeContext();
    }

    @Override
    public String seqId() {
        return seqId;
    }

    public DefaultInvokeContext seqId(String seqId) {
        this.seqId = seqId;
        return this;
    }

    @Override
    public long timeout() {
        return timeout;
    }

    public DefaultInvokeContext timeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    @Override
    public List<RpcChannelFuture> channelFutures() {
        return channelFutures;
    }

    public DefaultInvokeContext channelFutures(List<RpcChannelFuture> channelFutures) {
        this.channelFutures = channelFutures;
        return this;
    }

    @Override
    public String toString() {
        return "DefaultInvokeContext{" +
                "seqId='" + seqId + '\'' +
                ", timeout=" + timeout +
                ", channelFutures=" + channelFutures +
                '}';
    }

}
