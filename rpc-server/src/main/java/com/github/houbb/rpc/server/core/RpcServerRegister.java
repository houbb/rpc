///*
// * Copyright (c)  2019. houbinbin Inc.
// * rpc All rights reserved.
// */
//
//package com.github.houbb.rpc.server.core;
//
//import com.github.houbb.heaven.annotation.ThreadSafe;
//import com.github.houbb.log.integration.core.Log;
//import com.github.houbb.log.integration.core.LogFactory;
//import com.github.houbb.rpc.client.core.context.RpcClientContext;
//import io.netty.bootstrap.Bootstrap;
//import io.netty.channel.*;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.handler.codec.serialization.ClassResolvers;
//import io.netty.handler.codec.serialization.ObjectDecoder;
//import io.netty.handler.codec.serialization.ObjectEncoder;
//import io.netty.handler.logging.LogLevel;
//import io.netty.handler.logging.LoggingHandler;
//
///**
// * <p> rpc 服务端-注册中心 </p>
// *
// * <pre> Created: 2019/10/16 11:21 下午  </pre>
// * <pre> Project: rpc  </pre>
// *
// * @author houbinbin
// * @since 0.0.8
// */
//@ThreadSafe
//public class RpcServerRegister {
//
//    private static final Log LOG = LogFactory.getLog(RpcServerRegister.class);
//
//    /**
//     * 地址信息
//     * @since 0.0.8
//     */
//    private final String address;
//
//    /**
//     * 监听端口号
//     * @since 0.0.8
//     */
//    private final int port;
//
//    /**
//     * 客户端处理 handler
//     * 作用：用于获取请求信息
//     * @since 0.0.8
//     */
//    private final ChannelHandler channelHandler;
//
//    public RpcServerRegister(final String address,
//                             final int port,
//                             final ChannelHandler channelHandler) {
//        this.address = address;
//        this.port = port;
//        this.channelHandler = channelHandler;
//    }
//
//    /**
//     * 进行连接
//     * @since 0.0.6
//     */
//    public ChannelFuture connect() {
//        // 启动服务端
//        LOG.info("RPC 服务开始启动客户端");
//
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//
//        ChannelFuture channelFuture;
//        try {
//            Bootstrap bootstrap = new Bootstrap();
//            channelFuture = bootstrap.group(workerGroup)
//                    .channel(NioSocketChannel.class)
//                    .option(ChannelOption.SO_KEEPALIVE, true)
//                    .handler(new ChannelInitializer<Channel>(){
//                        @Override
//                        protected void initChannel(Channel ch) throws Exception {
//                            ch.pipeline()
//                                    // 解码 bytes=>resp
//                                    .addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)))
//                                    // request=>bytes
//                                    .addLast(new ObjectEncoder())
//                                    // 日志输出
//                                    .addLast(new LoggingHandler(LogLevel.INFO))
//                                    .addLast(channelHandler);
//                        }
//                    })
//                    .connect(address, port)
//                    .syncUninterruptibly();
//            log.info("RPC 服务启动客户端完成，监听地址 {}:{}", address, port);
//        } catch (Exception e) {
//            log.error("RPC 客户端遇到异常", e);
//            throw new RuntimeException(e);
//        }
//        // 不要关闭线程池！！！
//
//        return channelFuture;
//    }
//
//}
