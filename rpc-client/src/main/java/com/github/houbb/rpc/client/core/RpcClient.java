/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.client.core;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.client.handler.RpcClientHandler;
import com.github.houbb.rpc.common.constant.RpcConstant;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * <p> rpc 客户端 </p>
 *
 * <pre> Created: 2019/10/16 11:21 下午  </pre>
 * <pre> Project: rpc  </pre>
 *
 * @author houbinbin
 * @since 0.0.2
 */
public class RpcClient {

    private static final Log log = LogFactory.getLog(RpcClient.class);

    /**
     * 监听端口号
     */
    private final int port;

    /**
     * channel 信息
     * @since 0.0.4
     */
    private ChannelFuture channelFuture;

    /**
     * 客户端处理 handler
     * @since 0.0.4
     */
    private RpcClientHandler channelHandler;

    public RpcClient(int port) {
        this.port = port;
    }

    public RpcClient() {
        this(RpcConstant.PORT);
    }

    /**
     * 开始运行
     */
    public void start() {
        // 启动服务端
        log.info("RPC 服务开始启动客户端");

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            channelFuture = bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<Channel>(){
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            channelHandler = new RpcClientHandler();
                            ch.pipeline()
                                    .addLast(new LoggingHandler(LogLevel.INFO))
                                    .addLast(channelHandler);
                        }
                    })
                    .connect(RpcConstant.ADDRESS, port)
                    .syncUninterruptibly();
            log.info("RPC 服务启动客户端完成，监听端口：" + port);
        } catch (Exception e) {
            log.error("RPC 客户端遇到异常", e);
            throw new RuntimeException(e);
        }
        // 不要关闭线程池！！！
    }

//    /**
//     * 调用计算
//     * @param request 请求信息
//     * @return 结果
//     * @since 0.0.4
//     */
//    public CalculateResponse calculate(final CalculateRequest request) {
//        // 发送请求
//        final Channel channel = channelFuture.channel();
//        log.info("RPC 客户端发送请求，request: {}", request);
//
//        // 关闭当前线程，以获取对应的信息
//        // 使用序列化的方式
//        final byte[] bytes = JsonBs.serializeBytes(request);
//        ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
//
//        channel.writeAndFlush(byteBuf);
//        channel.closeFuture().syncUninterruptibly();
//
//        return channelHandler.getResponse();
//    }

}
