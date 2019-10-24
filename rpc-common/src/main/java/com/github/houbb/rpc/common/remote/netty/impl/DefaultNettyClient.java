package com.github.houbb.rpc.common.remote.netty.impl;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.common.exception.RpcRuntimeException;
import com.github.houbb.rpc.common.remote.netty.NettyClient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * netty 网络客户端
 * @author binbin.hou
 * @since 0.0.8
 */
public class DefaultNettyClient implements NettyClient {

    /**
     * 创建新的对象实例
     * @return 对象实例
     * @since 0.0.8
     */
    public static DefaultNettyClient newInstance() {
        return new DefaultNettyClient();
    }

    /**
     * 日志信息
     * @since 0.0.8
     */
    private static final Log LOG = LogFactory.getLog(DefaultNettyClient.class);

    @Override
    public ChannelFuture connect(String address, int port, final ChannelHandler channelHandler) {
        // 启动服务端
        LOG.info("[Netty Client] 开始启动客户端");

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ChannelFuture channelFuture;
        try {
            Bootstrap bootstrap = new Bootstrap();
            channelFuture = bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<Channel>(){
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(channelHandler);
                        }
                    })
                    .connect(address, port)
                    .syncUninterruptibly();
            LOG.info("[Netty Client] 启动客户端完成，监听地址 {}:{}", address, port);
        } catch (Exception e) {
            LOG.error("[Netty Client] 端遇到异常", e);
            throw new RpcRuntimeException(e);
        }
        // 不要关闭线程池！！！
        return channelFuture;
    }

    // 这里后期添加 destroy 可以添加线程池的关闭。

}
