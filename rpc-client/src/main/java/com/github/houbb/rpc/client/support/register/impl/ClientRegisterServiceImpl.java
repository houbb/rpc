/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.client.support.register.impl;

import com.github.houbb.heaven.support.handler.IHandler;
import com.github.houbb.heaven.util.common.ArgUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.client.handler.RpcClientRegisterHandler;
import com.github.houbb.rpc.client.invoke.InvokeService;
import com.github.houbb.rpc.client.support.register.ClientRegisterService;
import com.github.houbb.rpc.common.config.component.RpcAddress;
import com.github.houbb.rpc.common.remote.netty.handler.ChannelHandlerFactory;
import com.github.houbb.rpc.common.remote.netty.handler.ChannelHandlers;
import com.github.houbb.rpc.common.rpc.domain.RpcChannelFuture;
import com.github.houbb.rpc.common.rpc.domain.RpcResponse;
import com.github.houbb.rpc.common.rpc.domain.impl.RpcResponses;
import com.github.houbb.rpc.register.domain.entry.ServiceEntry;
import com.github.houbb.rpc.register.domain.entry.impl.ServiceEntryBuilder;
import com.github.houbb.rpc.register.domain.message.RegisterMessage;
import com.github.houbb.rpc.register.domain.message.impl.RegisterMessages;
import com.github.houbb.rpc.register.simple.constant.MessageTypeConst;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;

import java.util.List;

/**
 * <p> 默认客户端注册中心实现类 </p>
 *
 * <pre> Created: 2019/10/26 10:34 上午  </pre>
 * <pre> Project: rpc  </pre>
 *
 * @author houbinbin
 * @since 0.0.9
 */
public class ClientRegisterServiceImpl implements ClientRegisterService {

    private static final Log LOG = LogFactory.getLog(ClientRegisterServiceImpl.class);

    /**
     * 服务调用信息管理类
     *
     * @since 0.0.9
     */
    private final InvokeService invokeService;

    /**
     * 注册中心超时时间
     *
     * @since 0.0.8
     */
    private long registerCenterTimeOut;

    public ClientRegisterServiceImpl(InvokeService invokeService) {
        this.invokeService = invokeService;
        this.registerCenterTimeOut = 60 * 1000;
    }

    @Override
    public List<RpcAddress> queryServerAddressList(String serviceId, List<RpcAddress> registerCenterList) {
        //1. 参数校验
        ArgUtil.notEmpty(serviceId, "serviceId");
        ArgUtil.notEmpty(registerCenterList, "registerCenterList");

        //2. 查询服务信息
        List<ServiceEntry> serviceEntries = lookUpServiceEntryList(serviceId,
                registerCenterList);
        LOG.info("[Client] register center serviceEntries: {}", serviceEntries);

        //3. 结果转换
        return CollectionUtil.toList(serviceEntries, new IHandler<ServiceEntry, RpcAddress>() {
            @Override
            public RpcAddress handle(ServiceEntry serviceEntry) {
                return new RpcAddress(serviceEntry.ip(),
                        serviceEntry.port(), serviceEntry.weight());
            }
        });
    }

    /**
     * 查询服务信息列表
     *
     * @return 服务明细列表
     * @since 0.0.8
     */
    @SuppressWarnings("unchecked")
    private List<ServiceEntry> lookUpServiceEntryList(final String serviceId,
                                                      final List<RpcAddress> registerCenterList) {
        //1. 连接到注册中心
        List<RpcChannelFuture> channelFutureList = connectRegisterCenter(registerCenterList);

        //2. 选择一个
        // 直接取第一个即可，后续可以使用 load-balance 策略。
        ChannelFuture channelFuture = channelFutureList.get(0).channelFuture();

        //3. 发送查询请求
        ServiceEntry serviceEntry = ServiceEntryBuilder.of(serviceId);
        RegisterMessage registerMessage = RegisterMessages.of(MessageTypeConst.CLIENT_LOOK_UP, serviceEntry);

        //TODO: wiriteAndFlush()+addRequest() 可以优化成为一个方法
        final String seqId = registerMessage.seqId();
        invokeService.addRequest(seqId, registerCenterTimeOut);
        channelFuture.channel().writeAndFlush(registerMessage);

        //4. 等待查询结果
        RpcResponse rpcResponse = invokeService.getResponse(seqId);
        return (List<ServiceEntry>) RpcResponses.getResult(rpcResponse);
    }

    /**
     * 连接到注册中心
     *
     * @param registerCenterList 注册中心地址列表
     * @return 对应的结果列表
     * @since 0.0.8
     */
    private List<RpcChannelFuture> connectRegisterCenter(final List<RpcAddress> registerCenterList) {
        return ChannelHandlers.channelFutureList(registerCenterList,
                new ChannelHandlerFactory() {
                    @Override
                    public ChannelHandler handler() {
                        return ChannelHandlers.objectCodecHandler(new RpcClientRegisterHandler(invokeService));
                    }
                });
    }


}
