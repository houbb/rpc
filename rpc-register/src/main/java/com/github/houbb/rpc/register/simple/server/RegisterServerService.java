/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.register.simple.server;


import com.github.houbb.rpc.register.domain.entry.ServiceEntry;

import java.util.List;

/**
 * <p> 服务端注册服务类 </p>
 *
 * <pre> Created: 2019/10/23 9:08 下午  </pre>
 * <pre> Project: rpc  </pre>
 *
 * @author houbinbin
 * @since 0.0.8
 */
public interface RegisterServerService {

    /**
     * 注册当前服务信息
     * （1）将该服务通过 {@link ServiceEntry#serviceId()} 进行分组
     * 订阅了这个 serviceId 的所有客户端
     * @param serviceEntry 注册当前服务信息
     * @since 0.0.8
     * @return 更新后的服务信息列表
     */
    List<ServiceEntry> register(final ServiceEntry serviceEntry);

    /**
     * 注销当前服务信息
     * @param serviceEntry 注册当前服务信息
     * @since 0.0.8
     * @return 更新后的服务信息列表
     */
    List<ServiceEntry> unRegister(final ServiceEntry serviceEntry);

    /**
     * 根据服务标识发现对应的服务器信息
     * （1）如果对应的列表为空，则返回空列表。
     * @param serviceId 服务标识
     * @return 服务信息列表
     * @since 0.0.8
     */
    List<ServiceEntry> lookUp(final String serviceId);

}
