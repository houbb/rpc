package com.github.houbb.rpc.common.remote.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * channel 工具类
 * @author binbin.hou
 * @since 0.0.8
 */
public final class ChannelHandlers {

    private ChannelHandlers(){}

    /**
     * 包含默认 object 编码解码的 handler
     * @param channelHandlers 用户自定义 handler
     * @return channel handler
     * @since 0.0.8
     */
    public static ChannelHandler objectCodecHandler(final ChannelHandler ... channelHandlers) {
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline()
                        // 解码 bytes=>resp
                        .addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)))
                        // request=>bytes
                        .addLast(new ObjectEncoder())
                        .addLast(channelHandlers);
            }
        };
    }

    /**
     * 包含默认 object 编码解码的 + 日志 handler
     * @param channelHandlers 用户自定义 handler
     * @return channel handler
     * @since 0.0.8
     */
    public static ChannelHandler objectCodecLogHandler(final ChannelHandler ... channelHandlers) {
        final ChannelHandler objectCodecHandler = objectCodecHandler(channelHandlers);
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline()
                        .addLast(new LoggingHandler(LogLevel.INFO))
                        .addLast(objectCodecHandler)
                        .addLast(channelHandlers);
            }
        };
    }

}
