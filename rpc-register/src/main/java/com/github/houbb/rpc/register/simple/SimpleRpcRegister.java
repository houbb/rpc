/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.register.simple;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.common.rpc.domain.RpcResponse;
import com.github.houbb.rpc.common.rpc.domain.impl.DefaultRpcResponse;
import com.github.houbb.rpc.register.domain.entry.ServiceEntry;
import com.github.houbb.rpc.register.simple.client.ClientRegisterService;
import com.github.houbb.rpc.register.simple.server.ServerRegisterService;
import com.github.houbb.rpc.register.simple.server.impl.DefaultServerRegisterService;
import com.github.houbb.rpc.register.spi.RpcRegister;
import io.netty.channel.Channel;

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
    public void register(ServiceEntry serviceEntry) {
        List<ServiceEntry> serviceEntryList = serverRegisterService.register(serviceEntry);

        // 通知监听者
        clientRegisterService.notify(serviceEntry.serviceId(), serviceEntryList);
    }

    @Override
    public void unRegister(ServiceEntry serviceEntry) {
        List<ServiceEntry> serviceEntryList = serverRegisterService.unRegister(serviceEntry);

        // 通知监听者
        clientRegisterService.notify(serviceEntry.serviceId(), serviceEntryList);
    }

    @Override
    public void subscribe(ServiceEntry clientEntry, final Channel channel) {
        clientRegisterService.subscribe(clientEntry, channel);
    }

    @Override
    public void unSubscribe(ServiceEntry clientEntry, Channel channel) {
        clientRegisterService.unSubscribe(clientEntry, channel);
    }

    @Override
    public void lookUp(String seqId, ServiceEntry clientEntry, Channel channel) {
        final String serviceId = clientEntry.serviceId();
        List<ServiceEntry> serviceEntryList = serverRegisterService.lookUp(serviceId);

        // 回写
        // 为了复用原先的相应结果，此处直接使用 rpc response
        RpcResponse rpcResponse = DefaultRpcResponse.newInstance().seqId(seqId)
                .result(serviceEntryList);
        channel.writeAndFlush(rpcResponse);
    }


}
