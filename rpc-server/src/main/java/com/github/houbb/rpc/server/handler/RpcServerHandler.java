package com.github.houbb.rpc.server.handler;

import com.github.houbb.json.bs.JsonBs;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.common.model.CalculateRequest;
import com.github.houbb.rpc.common.model.CalculateResponse;
import com.github.houbb.rpc.common.service.Calculator;
import com.github.houbb.rpc.server.service.CalculatorService;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author binbin.hou
 * @since 0.0.1
 */
public class RpcServerHandler extends SimpleChannelInboundHandler {

    private static final Log log = LogFactory.getLog(RpcServerHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        final String id = ctx.channel().id().asLongText();
        log.info("[Server] channel {} connected " + id);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        final String id = ctx.channel().id().asLongText();

        ByteBuf byteBuf = (ByteBuf)msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        String json = new String(bytes);
        log.info("[Server] receive request json: {} ", json);
        CalculateRequest request = JsonBs.deserialize(json, CalculateRequest.class);
        log.info("[Server] receive channel {} request: {} from ", id, request);

        Calculator calculator = new CalculatorService();
        CalculateResponse response = calculator.sum(request);

        // 回写到 client 端
        String responseJson = JsonBs.serialize(response);
        ByteBuf responseBuffer = Unpooled.copiedBuffer(responseJson.getBytes());
        ctx.writeAndFlush(responseBuffer);
        log.info("[Server] channel {} response {}", id, response);
    }

}
