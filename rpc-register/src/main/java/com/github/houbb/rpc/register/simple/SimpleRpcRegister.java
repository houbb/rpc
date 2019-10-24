/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.register.simple;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.register.api.RpcRegister;
import com.github.houbb.rpc.register.domain.entry.ServiceEntry;
import com.github.houbb.rpc.register.domain.message.RegisterMessage;
import com.github.houbb.rpc.register.domain.message.impl.RegisterMessages;
import com.github.houbb.rpc.register.simple.client.ClientRegisterService;
import com.github.houbb.rpc.register.simple.constant.MessageTypeConst;
import com.github.houbb.rpc.register.simple.server.ServerRegisterService;
import com.github.houbb.rpc.register.simple.server.impl.DefaultServerRegisterService;
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
    public void lookUp(ServiceEntry clientEntry, Channel channel) {
        final String serviceId = clientEntry.serviceId();
        List<ServiceEntry> serviceEntryList = serverRegisterService.lookUp(serviceId);

        // 回写
        RegisterMessage registerMessage = RegisterMessages.of(MessageTypeConst.REGISTER_LOOK_UP_RESP, serviceEntryList);
        channel.writeAndFlush(registerMessage);
    }


}
