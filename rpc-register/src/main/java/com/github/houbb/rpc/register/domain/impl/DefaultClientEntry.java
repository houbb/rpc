/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.register.domain.impl;

import com.github.houbb.rpc.register.domain.ClientEntry;

/**
 * <p> 默认客户端明细 </p>
 *
 * <pre> Created: 2019/10/23 8:26 下午  </pre>
 * <pre> Project: rpc  </pre>
 *
 * @author houbinbin
 * @since 0.0.8
 */
public class DefaultClientEntry implements ClientEntry {

    private static final long serialVersionUID = 189302740666003309L;

    /**
     * 服务标识
     * @since 0.0.8
     */
    private String serviceId;


    /**
     * 机器 ip 信息
     *
     * <pre>
     *     InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
     *     String clientIP = insocket.getAddress().getHostAddress();
     * </pre>
     *
     * @since 0.0.8
     */
    private String ip;

    @Override
    public String serviceId() {
        return serviceId;
    }

    public DefaultClientEntry serviceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    @Override
    public String ip() {
        return ip;
    }

    public DefaultClientEntry ip(String ip) {
        this.ip = ip;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultClientEntry that = (DefaultClientEntry) o;

        if (serviceId != null ? !serviceId.equals(that.serviceId) : that.serviceId != null)
            return false;
        return ip != null ? ip.equals(that.ip) : that.ip == null;
    }

    @Override
    public int hashCode() {
        int result = serviceId != null ? serviceId.hashCode() : 0;
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DefaultClientEntry{" +
                "serviceId='" + serviceId + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }

}
