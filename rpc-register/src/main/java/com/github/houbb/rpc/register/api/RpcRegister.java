/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.register.api;

import com.github.houbb.rpc.register.domain.ClientEntry;
import com.github.houbb.rpc.register.domain.ServerEntry;

import java.util.List;

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
     * （1）将该服务通过 {@link ServerEntry#serviceId()} 进行分组
     * （2）每次发生变化，通知 {@link #subscribe(ClientEntry)} 中客户端
     * 订阅了这个 serviceId 的所有客户端
     * @param serverEntry 注册当前服务信息
     * @since 0.0.8
     */
    void register(final ServerEntry serverEntry);

    /**
     * 注销当前服务信息
     * @param serverEntry 注册当前服务信息
     * @since 0.0.8
     */
    void unRegister(final ServerEntry serverEntry);

    /**
     * 监听服务信息
     * （1）监听之后，如果有任何相关的机器信息发生变化，则进行推送。
     * （2）内置的信息，需要传送 ip 信息到注册中心。
     *
     * @param clientEntry 客户端明细信息
     * @since 0.0.8
     */
    void subscribe(final ClientEntry clientEntry);

    /**
     * 取消监听服务信息
     *
     * （1）将改服务从客户端的监听列表中移除即可。
     *
     * @param clientEntry 客户端明细信息
     * @since 0.0.8
     */
    void unSubscribe(final ClientEntry clientEntry);

}
