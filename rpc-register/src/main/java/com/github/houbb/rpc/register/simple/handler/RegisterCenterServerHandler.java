/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.register.simple.handler;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.register.domain.entry.ServiceEntry;
import com.github.houbb.rpc.register.domain.message.RegisterMessage;
import com.github.houbb.rpc.register.domain.message.impl.RegisterMessages;
import com.github.houbb.rpc.register.simple.SimpleRpcRegister;
import com.github.houbb.rpc.register.simple.client.ClientRegisterService;
import com.github.houbb.rpc.register.simple.client.impl.DefaultClientRegisterService;
import com.github.houbb.rpc.register.simple.constant.MessageTypeConst;
import com.github.houbb.rpc.register.simple.server.ServerRegisterService;
import com.github.houbb.rpc.register.simple.server.impl.DefaultServerRegisterService;
import com.github.houbb.rpc.register.spi.RpcRegister;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * <p> 注册中心服务器处理类 </p>
 *
 * <pre> Created: 2019/10/23 10:29 下午  </pre>
 * <pre> Project: rpc  </pre>
 * <p>
 * 请求的标准化：
 * （1）对于 server 的服务注册，client 的配置拉取。
 * 二者都是将 register 作为服务端。所以需要统一请求信息。
 * （2）对于 server 的注册，不需要提供对应的反馈信息
 * （3）当配置发生变化时，需要及时通知所有的 client 端。
 * 这里就需要知道哪些是客户端？？
 *
 * @author houbinbin
 * @since 0.0.8
 */
@ChannelHandler.Sharable
public class RegisterCenterServerHandler extends SimpleChannelInboundHandler {

    private static final Log LOG = LogFactory.getLog(RegisterCenterServerHandler.class);

    /**
     * 注册中心服务
     * @since 0.0.8
     */
    private final RpcRegister rpcRegister;

    public RegisterCenterServerHandler() {
        this.rpcRegister = this.buildSimpleRpcRegister();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        final String id = ctx.channel().id().asLongText();
        LOG.info("[Register Server] channel {} connected " + id);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        RegisterMessage registerMessage = (RegisterMessage) msg;
        Object body = registerMessage.body();
        int type = RegisterMessages.type(registerMessage);
        String seqId = RegisterMessages.seqId(registerMessage);
        LOG.info("[Register Server] received message type: {}, seqId: {} ", type,
                seqId);

        final Channel channel = ctx.channel();
        ServiceEntry serviceEntry = (ServiceEntry)body;

        switch (type) {
            case MessageTypeConst.SERVER_REGISTER:
                rpcRegister.register(serviceEntry);
                break;

            case MessageTypeConst.SERVER_UN_REGISTER:
                rpcRegister.unRegister(serviceEntry);
                break;

            case MessageTypeConst.CLIENT_SUBSCRIBE:
                rpcRegister.subscribe(serviceEntry, channel);
                break;

            case MessageTypeConst.CLIENT_UN_SUBSCRIBE:
                rpcRegister.unSubscribe(serviceEntry, channel);
                break;

            case MessageTypeConst.CLIENT_LOOK_UP:
                rpcRegister.lookUp(seqId, serviceEntry, channel);
                break;

            default:
                LOG.warn("[Register Center] not support type: {} and seqId: {}",
                        type, seqId);
        }

    }

    /**
     * 构建简单注册实现类
     * @return 注册实现
     * @since 0.0.8
     */
    private RpcRegister buildSimpleRpcRegister() {
        final ServerRegisterService serverRegisterService = new DefaultServerRegisterService();
        final ClientRegisterService clientRegisterService = new DefaultClientRegisterService();
        return new SimpleRpcRegister(serverRegisterService, clientRegisterService);
    }



}
