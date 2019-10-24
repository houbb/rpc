package com.github.houbb.rpc.common.remote.netty;

import io.netty.channel.ChannelHandler;

/**
 * netty 网络服务端
 * @author binbin.hou
 * @since 0.0.8
 */
public interface NettyServer {

    /**
     * 服务启动
     * TODO: 这里有一个问题，强耦合了 netty。
     *
     * 即使 Netty 是现在最好的 java 通讯框架，但是丢失了灵活性。
     * @param port 端口信息
     * @param channelHandler 处理类
     * @since 0.0.8
     */
    void start(final int port, final ChannelHandler channelHandler);

}
