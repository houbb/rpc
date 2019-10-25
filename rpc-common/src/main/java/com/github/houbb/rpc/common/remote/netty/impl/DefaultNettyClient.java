package com.github.houbb.rpc.common.remote.netty.impl;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.common.exception.RpcRuntimeException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * netty 网络客户端
 * @author binbin.hou
 * @since 0.0.8
 */
public class DefaultNettyClient extends AbstractNettyClient<ChannelFuture> {

    /**
     * 工作线程池
     * @since 0.0.8
     */
    private EventLoopGroup workerGroup;

    /**
     * channel 信息
     * @since 0.0.8
     */
    private ChannelFuture channelFuture;

    private DefaultNettyClient(String ip, int port, ChannelHandler channelHandler) {
        super(ip, port, channelHandler);
    }

    /**
     * 创建新的对象实例
     * @return 对象实例
     * @since 0.0.8
     */
    public static DefaultNettyClient newInstance(String ip, int port,
                                                 ChannelHandler channelHandler) {
        return new DefaultNettyClient(ip, port, channelHandler);
    }

    /**
     * 日志信息
     * @since 0.0.8
     */
    private static final Log LOG = LogFactory.getLog(DefaultNettyClient.class);

    @Override
    public ChannelFuture call() {
        // 启动服务端
        LOG.info("[Netty Client] 开始启动客户端");

        workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            channelFuture = bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(channelHandler)
                    .connect(ip, port)
                    .syncUninterruptibly();
            LOG.info("[Netty Client] 启动客户端完成，监听地址 {}:{}", ip, port);
        } catch (Exception e) {
            LOG.error("[Netty Client] 端启动遇到异常", e);
            throw new RpcRuntimeException(e);
        }
        // 不要关闭线程池！！！
        return channelFuture;
    }

    @Override
    public void destroy() {
        try {
            channelFuture.channel().closeFuture().syncUninterruptibly();
            LOG.info("[Netty Client] 关闭完成");
        } catch (Exception e) {
            LOG.error("[Netty Client] 关闭服务异常", e);
            throw new RpcRuntimeException(e);
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

}
