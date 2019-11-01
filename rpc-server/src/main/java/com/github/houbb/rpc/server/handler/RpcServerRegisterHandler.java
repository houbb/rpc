package com.github.houbb.rpc.server.handler;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 注册中心
 * （1）用于和注册中心建立长连接。
 * （2）初期设计中服务端不需要做什么事情。
 *
 * 后期可以调整为接收到影响为准，保证请求成功。
 * @author binbin.hou
 * @since 0.0.8
 */
public class RpcServerRegisterHandler extends SimpleChannelInboundHandler {

    private static final Log LOG = LogFactory.getLog(RpcServerRegisterHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOG.info("[Rpc Server] received message: {}", msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.error("[Rpc Server] meet ex", cause);
        ctx.close();
    }

}
