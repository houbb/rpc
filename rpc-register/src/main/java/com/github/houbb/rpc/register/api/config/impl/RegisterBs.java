package com.github.houbb.rpc.register.api.config.impl;

import com.github.houbb.heaven.util.common.ArgUtil;
import com.github.houbb.rpc.common.remote.netty.handler.ChannelHandlers;
import com.github.houbb.rpc.common.remote.netty.impl.DefaultNettyServer;
import com.github.houbb.rpc.register.api.config.RegisterConfig;
import com.github.houbb.rpc.register.simple.handler.RegisterCenterServerHandler;
import io.netty.channel.ChannelHandler;

/**
 * 默认注册中心配置
 * @author binbin.hou
 * @since 0.0.8
 */
public class RegisterBs implements RegisterConfig {

    /**
     * 服务启动端口信息
     * @since 0.0.8
     */
    private int port;

    private RegisterBs(){}

    public static RegisterBs newInstance() {
        RegisterBs registerBs = new RegisterBs();
        registerBs.port(8527);
        return registerBs;
    }

    @Override
    public RegisterBs port(int port) {
        ArgUtil.notNegative(port, "port");

        this.port = port;
        return this;
    }

    @Override
    public RegisterBs start() {
        ChannelHandler channelHandler = ChannelHandlers.objectCodecHandler(new RegisterCenterServerHandler());
        DefaultNettyServer.newInstance(port, channelHandler).asyncRun();

        return this;
    }

}
