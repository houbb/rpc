/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.register.simple.client;

import com.github.houbb.rpc.register.domain.ClientEntry;
import com.github.houbb.rpc.register.domain.ServerEntry;

import java.util.List;

/**
 * <p> 客户端注册服务类 </p>
 *
 * <pre> Created: 2019/10/23 9:08 下午  </pre>
 * <pre> Project: rpc  </pre>
 *
 * @author houbinbin
 * @since 0.0.8
 */
public interface ClientRegisterService {

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

    /**
     * 客户端列表信息
     * @param serviceId 服务标识
     * @return 客户端列表信息
     * @since 0.0.8
     */
    List<String> clientList(final String serviceId);

}
