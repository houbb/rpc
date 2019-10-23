/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.register.simple;

import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.register.api.RpcRegister;
import com.github.houbb.rpc.register.domain.ClientEntry;
import com.github.houbb.rpc.register.domain.ServerEntry;
import com.github.houbb.rpc.register.simple.client.ClientRegisterService;
import com.github.houbb.rpc.register.simple.server.ServerRegisterService;
import com.github.houbb.rpc.register.simple.server.impl.DefaultServerRegisterService;

import java.util.List;

/**
 * <p> 简单的 rpc 注册 </p>
 *
 * <pre> Created: 2019/10/23 8:59 下午  </pre>
 * <pre> Project: rpc  </pre>
 *
 * （1）各种关系的关系服务类
 * （2）各种关系之间的通讯类
 * （3）domain 层
 *
 * @author houbinbin
 * @since 0.0.8
 */
public class SimpleRpcRegister implements RpcRegister {

    private static final Log LOG = LogFactory.getLog(DefaultServerRegisterService.class);

    /**
     * 服务端信息管理
     * @since 0.0.8
     */
    private final ServerRegisterService serverRegisterService;

    /**
     * 客户端信息管理
     * @since 0.0.8
     */
    private final ClientRegisterService clientRegisterService;

    public SimpleRpcRegister(ServerRegisterService serverRegisterService, ClientRegisterService clientRegisterService) {
        this.serverRegisterService = serverRegisterService;
        this.clientRegisterService = clientRegisterService;
    }

    @Override
    public void register(ServerEntry serverEntry) {
        List<ServerEntry> serverEntryList = serverRegisterService.register(serverEntry);

        // 通知监听者
        notifyListener(serverEntry.serviceId(), serverEntryList);
    }

    @Override
    public void unRegister(ServerEntry serverEntry) {
        List<ServerEntry> serverEntryList = serverRegisterService.unRegister(serverEntry);

        // 通知监听者
        notifyListener(serverEntry.serviceId(), serverEntryList);
    }

    @Override
    public void subscribe(ClientEntry clientEntry) {
        clientRegisterService.subscribe(clientEntry);
    }

    @Override
    public void unSubscribe(ClientEntry clientEntry) {
        clientRegisterService.unSubscribe(clientEntry);
    }

    /**
     * 通知监听者
     *
     * @param serviceId       服务标识
     * @param serverEntryList 服务端明细信息
     * @since 0.0.8
     */
    private void notifyListener(final String serviceId, final List<ServerEntry> serverEntryList) {
        // 列表信息变化，一定要通知。
        // 可以后期添加优化，标识出是否发生变化。

        // 通知客户端变化
        List<String> clientHosts = clientRegisterService.clientList(serviceId);
        if (CollectionUtil.isEmpty(clientHosts)) {
            LOG.info("[Register] notify clients is empty for service: {}",
                    serviceId);
            return;
        }

        // 循环获取对应的 ctx.writeAndFlush()
        // 这里还缺少两样东西：
        // (1) 服务端与注册中心的网络通信
        // (2) 客户端与注册中心的网络通信
    }

}
