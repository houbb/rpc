package com.github.houbb.rpc.server.handler;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.common.rpc.domain.RpcRequest;
import com.github.houbb.rpc.common.rpc.domain.impl.DefaultRpcResponse;
import com.github.houbb.rpc.server.service.impl.DefaultServiceFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author binbin.hou
 * @since 0.0.1
 */
@ChannelHandler.Sharable
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
        log.info("[Server] channel read start: {}", id);

        // 接受客户端请求
        RpcRequest rpcRequest = (RpcRequest)msg;
        log.info("[Server] receive channel {} request: {}", id, rpcRequest);

        // 回写到 client 端
        DefaultRpcResponse rpcResponse = handleRpcRequest(rpcRequest);
        ctx.writeAndFlush(rpcResponse);
        log.info("[Server] channel {} response {}", id, rpcResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("[Server] meet ex: ", cause);
        ctx.close();
    }

    /**
     * 处理请求信息
     * @param rpcRequest 请求信息
     * @return 结果信息
     * @since 0.0.6
     */
    private DefaultRpcResponse handleRpcRequest(final RpcRequest rpcRequest) {
        DefaultRpcResponse rpcResponse = new DefaultRpcResponse();
        rpcResponse.seqId(rpcRequest.seqId());

        try {
            // 获取对应的 service 实现类
            // rpcRequest=>invocationRequest
            // 执行 invoke
            Object result = DefaultServiceFactory.getInstance()
                    .invoke(rpcRequest.serviceId(),
                            rpcRequest.methodName(),
                            rpcRequest.paramTypeNames(),
                            rpcRequest.paramValues());
            rpcResponse.result(result);
        } catch (Exception e) {
            rpcResponse.error(e);
            log.error("[Server] execute meet ex for request", rpcRequest, e);
        }

        // 构建结果值
        return rpcResponse;
    }

}
