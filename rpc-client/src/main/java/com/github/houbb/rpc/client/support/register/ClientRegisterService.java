/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.client.support.register;

import com.github.houbb.rpc.common.config.component.RpcAddress;

import java.util.List;

/**
 * <p> 客户端注册中心服务接口 </p>
 *
 * <pre> Created: 2019/10/26 10:27 上午  </pre>
 * <pre> Project: rpc  </pre>
 *
 * @author houbinbin
 * @since 0.0.9
 */
public interface ClientRegisterService {

    /**
     * 查询服务端地址信息列表
     * （1）registerCenterTimeOut 超时时间内部直接指定
     * @param serviceId 服务唯一标识
     * @param registerCenterList 注册中心列表
     * @return 服务端地址信息列表
     * @since 0.0.9
     */
    List<RpcAddress> queryServerAddressList(final String serviceId,
                                            final List<RpcAddress> registerCenterList);

}
