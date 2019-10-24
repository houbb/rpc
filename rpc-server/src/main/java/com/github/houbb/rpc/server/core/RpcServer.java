//package com.github.houbb.rpc.server.core;
//
//import com.github.houbb.log.integration.core.Log;
//import com.github.houbb.log.integration.core.LogFactory;
//import com.github.houbb.rpc.common.constant.RpcConstant;
//import com.github.houbb.rpc.server.handler.RpcServerHandler;
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.*;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import io.netty.handler.codec.serialization.ClassResolvers;
//import io.netty.handler.codec.serialization.ObjectDecoder;
//import io.netty.handler.codec.serialization.ObjectEncoder;
//import io.netty.handler.logging.LogLevel;
//import io.netty.handler.logging.LoggingHandler;
//
///**
// * rpc 服务端
// * @author binbin.hou
// * @since 0.0.1
// */
//@Deprecated
//public class RpcServer extends Thread {
//
//    private static final Log log = LogFactory.getLog(RpcServer.class);
//
//    /**
//     * 端口号
//     */
//    private final int port;
//
//    public RpcServer() {
//        this.port = RpcConstant.PORT;
//    }
//
//    public RpcServer(int port) {
//        this.port = port;
//    }
//
//    @Override
//    public void run() {
//        // 启动服务端
//        log.info("RPC 服务开始启动服务端");
//
//        EventLoopGroup bossGroup = new NioEventLoopGroup();
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//
//        try {
//            ServerBootstrap serverBootstrap = new ServerBootstrap();
//            serverBootstrap.group(workerGroup, bossGroup)
//                    .channel(NioServerSocketChannel.class)
//                    // 打印日志
//                    .handler(new LoggingHandler(LogLevel.INFO))
//                    .childHandler(new ChannelInitializer<Channel>() {
//                        @Override
//                        protected void initChannel(Channel ch) throws Exception {
//                            ch.pipeline()
//                            // 解码 bytes=>resp
//                            .addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)))
//                             // request=>bytes
//                             .addLast(new ObjectEncoder())
//                             .addLast(new RpcServerHandler());
//                        }
//                    })
//                    // 这个参数影响的是还没有被accept 取出的连接
//                    .option(ChannelOption.SO_BACKLOG, 128)
//                    // 这个参数只是过一段时间内客户端没有响应，服务端会发送一个 ack 包，以判断客户端是否还活着。
//                    .childOption(ChannelOption.SO_KEEPALIVE, true);
//
//            // 绑定端口，开始接收进来的链接
//            ChannelFuture channelFuture = serverBootstrap.bind(port).syncUninterruptibly();
//            log.info("RPC 服务端启动完成，监听【" + port + "】端口");
//
//            channelFuture.channel().closeFuture().syncUninterruptibly();
//            log.info("RPC 服务端关闭完成");
//        } catch (Exception e) {
//            log.error("RPC 服务异常", e);
//        } finally {
//            workerGroup.shutdownGracefully();
//            bossGroup.shutdownGracefully();
//        }
//    }
//
//
//}
