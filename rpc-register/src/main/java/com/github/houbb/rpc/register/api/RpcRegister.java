/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.register.api;


import com.github.houbb.rpc.register.domain.entry.ServiceEntry;
import io.netty.channel.Channel;

/**
 * <p> 注册中心接口 </p>
 *
 * <pre> Created: 2019/10/23 8:01 下午  </pre>
 * <pre> Project: rpc  </pre>
 *
 * @author houbinbin
 * @since 0.0.8
 */
public interface RpcRegister {

    /**
     * 注册当前服务信息
     * 订阅了这个 serviceId 的所有客户端
     * @param serviceEntry 注册当前服务信息
     * @since 0.0.8
     */
    void register(final ServiceEntry serviceEntry);

    /**
     * 注销当前服务信息
     * @param serviceEntry 注册当前服务信息
     * @since 0.0.8
     */
    void unRegister(final ServiceEntry serviceEntry);

    /**
     * 监听服务信息
     * （1）监听之后，如果有任何相关的机器信息发生变化，则进行推送。
     * （2）内置的信息，需要传送 ip 信息到注册中心。
     *
     * @param serviceEntry 客户端明细信息
     * @param channel 频道信息
     * @since 0.0.8
     */
    void subscribe(final ServiceEntry serviceEntry, final Channel channel);

    /**
     * 取消监听服务信息
     *
     * （1）将改服务从客户端的监听列表中移除即可。
     *
     * @param server 客户端明细信息
     * @param channel 频道信息
     * @since 0.0.8
     */
    void unSubscribe(final ServiceEntry server, final Channel channel);

    /**
     * 启动时查询 serviceId 对应的所有服务端信息
     * @param clientEntry 客户端查询明细
     * @param channel 频道信息
     * @since 0.0.8
     */
    void lookUp(ServiceEntry clientEntry, final Channel channel);

}
