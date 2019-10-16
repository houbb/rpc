/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.client.core;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.client.handler.RpcClientHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
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
public class RpcClient extends Thread {

    private static final Log log = LogFactory.getLog(RpcClient.class);

    /**
     * 监听端口号
     */
    private final int port;

    public RpcClient(int port) {
        this.port = port;
    }

    public RpcClient() {
        this(9527);
    }

    @Override
    public void run() {
        // 启动服务端
        log.info("RPC 服务开始启动客户端");

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            ChannelFuture channelFuture = bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<Channel>(){
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new LoggingHandler(LogLevel.INFO))
                                    .addLast(new RpcClientHandler());
                        }
                    })
                    .connect("localhost", port)
                    .syncUninterruptibly();

            log.info("RPC 服务启动客户端完成，监听端口：" + port);
            channelFuture.channel().closeFuture().syncUninterruptibly();
            log.info("RPC 服务开始客户端已关闭");
        } catch (Exception e) {
            log.error("RPC 客户端遇到异常", e);
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

}
