package com.github.houbb.rpc.server.handler;

import com.github.houbb.json.bs.JsonBs;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.common.rpc.domain.RpcRequest;
import com.github.houbb.rpc.common.rpc.domain.impl.DefaultRpcRequest;
import com.github.houbb.rpc.common.rpc.domain.impl.DefaultRpcResponse;
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

        DefaultRpcRequest rpcRequest = JsonBs.deserializeBytes(bytes, DefaultRpcRequest.class);
        log.info("[Server] receive channel {} request: {}", id, rpcRequest);

        DefaultRpcResponse rpcResponse = handleRpcRequest(rpcRequest);

        // 回写到 client 端
        byte[] responseBytes = JsonBs.serializeBytes(rpcResponse);
        ByteBuf responseBuffer = Unpooled.copiedBuffer(responseBytes);
        ctx.writeAndFlush(responseBuffer);
        log.info("[Server] channel {} response {}", id, rpcResponse);
    }

    /**
     * 处理请求信息
     * @param rpcRequest 请求信息
     * @return 结果信息
     * @since 0.0.6
     */
    private DefaultRpcResponse handleRpcRequest(final RpcRequest rpcRequest) {
        DefaultRpcResponse rpcResponse = new DefaultRpcResponse();
        // 获取对应的 service 实现类
        // rpcRequest=>invocationRequest
        // 执行 invoke

        // 构建结果值
        return rpcResponse;
    }

}
