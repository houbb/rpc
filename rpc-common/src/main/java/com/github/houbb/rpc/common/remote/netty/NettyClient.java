package com.github.houbb.rpc.common.remote.netty;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;

/**
 * netty 网络客户端
 * @author binbin.hou
 * @since 0.0.8
 */
public interface NettyClient {

    /**
     * 连接服务
     * @param address 服务地址
     * @param port 服务端口号
     * @param channelHandler 处理器
     * @since 0.0.8
     * @return 返回值信息
     */
    ChannelFuture connect(final String address,
                          final int port,
                          final ChannelHandler channelHandler);

}
