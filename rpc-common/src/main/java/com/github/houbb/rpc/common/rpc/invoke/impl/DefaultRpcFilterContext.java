/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.common.rpc.invoke.impl;

import com.github.houbb.rpc.common.rpc.domain.BaseRpc;
import com.github.houbb.rpc.common.rpc.domain.RpcChannelFuture;
import com.github.houbb.rpc.common.rpc.invoke.RpcFilterContext;

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
public class DefaultRpcFilterContext implements RpcFilterContext {

    /**
     * 请求信息
     * @since 0.0.9
     */
    private BaseRpc request;

    /**
     * 调用唯一标识
     * @since 0.0.9
     */
    private String  seqId;

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
    public static DefaultRpcFilterContext newInstance() {
        return new DefaultRpcFilterContext();
    }

    @Override
    public BaseRpc request() {
        return request;
    }

    public DefaultRpcFilterContext request(BaseRpc request) {
        this.request = request;
        return this;
    }

    @Override
    public String seqId() {
        return seqId;
    }

    public DefaultRpcFilterContext seqId(String seqId) {
        this.seqId = seqId;
        return this;
    }

    @Override
    public long timeout() {
        return timeout;
    }

    public DefaultRpcFilterContext timeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    @Override
    public List<RpcChannelFuture> channelFutures() {
        return channelFutures;
    }

    public DefaultRpcFilterContext channelFutures(List<RpcChannelFuture> channelFutures) {
        this.channelFutures = channelFutures;
        return this;
    }

    @Override
    public String toString() {
        return "DefaultInvokeContext{" +
                "request=" + request +
                ", seqId='" + seqId + '\'' +
                ", timeout=" + timeout +
                ", channelFutures=" + channelFutures +
                '}';
    }
}
