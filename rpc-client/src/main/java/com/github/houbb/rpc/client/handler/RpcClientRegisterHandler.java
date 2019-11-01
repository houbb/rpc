/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.client.handler;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.client.invoke.InvokeService;
import com.github.houbb.rpc.common.rpc.domain.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * <p> 客户端注册中心处理类 </p>
 *
 * <pre> Created: 2019/10/16 11:30 下午  </pre>
 * <pre> Project: rpc  </pre>
 *
 * @author houbinbin
 * @since 0.0.8
 */
public class RpcClientRegisterHandler extends SimpleChannelInboundHandler {

    private static final Log log = LogFactory.getLog(RpcClientRegisterHandler.class);

    /**
     * 注册服务
     * @since 0.0.8
     */
    private final InvokeService invokeService;

    public RpcClientRegisterHandler(InvokeService invokeService) {
        this.invokeService = invokeService;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcResponse rpcResponse = (RpcResponse) msg;
        log.info("[Client Register] response is :{}", rpcResponse);
        invokeService.addResponse(rpcResponse.seqId(), rpcResponse);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 每次用完要关闭，不然拿不到response，我也不知道为啥（目测得了解netty才行）
        // 个人理解：如果不关闭，则永远会被阻塞。
        ctx.flush();
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("[Rpc Client] meet ex ", cause);
        ctx.close();
    }

}
