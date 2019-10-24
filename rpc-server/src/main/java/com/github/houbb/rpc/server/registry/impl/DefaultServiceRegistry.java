package com.github.houbb.rpc.server.registry.impl;

import com.github.houbb.heaven.util.common.ArgUtil;
import com.github.houbb.heaven.util.guava.Guavas;
import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.rpc.common.config.component.RpcAddress;
import com.github.houbb.rpc.common.config.component.RpcAddressBuilder;
import com.github.houbb.rpc.common.config.protocol.ProtocolConfig;
import com.github.houbb.rpc.common.remote.netty.handler.ChannelHandlers;
import com.github.houbb.rpc.common.remote.netty.impl.DefaultNettyClient;
import com.github.houbb.rpc.common.remote.netty.impl.DefaultNettyServer;
import com.github.houbb.rpc.register.domain.entry.ServiceEntry;
import com.github.houbb.rpc.server.config.service.DefaultServiceConfig;
import com.github.houbb.rpc.server.config.service.ServiceConfig;
import com.github.houbb.rpc.server.handler.RpcServerHandler;
import com.github.houbb.rpc.server.handler.RpcServerRegisterHandler;
import com.github.houbb.rpc.server.registry.ServiceRegistry;
import com.github.houbb.rpc.server.service.impl.DefaultServiceFactory;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认服务端注册类
 * @author binbin.hou
 * @since 0.0.6
 */
public class DefaultServiceRegistry implements ServiceRegistry {

    /**
     * 单例信息
     * @since 0.0.6
     */
    private static final DefaultServiceRegistry INSTANCE = new DefaultServiceRegistry();

    /**
     * rpc 服务端端口号
     * @since 0.0.6
     */
    private int rpcPort;

    /**
     * 协议配置
     * （1）默认只实现 tcp
     * （2）后期可以拓展实现 web-service/http/https 等等。
     * @since 0.0.6
     */
    private ProtocolConfig protocolConfig;

    /**
     * 服务配置列表
     * @since 0.0.6
     */
    private List<ServiceConfig> serviceConfigList;

    /**
     * 注册中心地址列表
     * @since 0.0.8
     */
    private List<RpcAddress> registerAddressList;

    /**
     * 是否注册到注册中心
     * @since 0.0.8
     */
    private boolean register;

    private DefaultServiceRegistry(){
        // 初始化默认参数
        this.serviceConfigList = new ArrayList<>();
        this.rpcPort = 9527;
        this.registerAddressList = Guavas.newArrayList();
        this.register = true;
    }

    public static DefaultServiceRegistry getInstance() {
        return INSTANCE;
    }

    @Override
    public ServiceRegistry port(int port) {
        ArgUtil.positive(port, "port");

        this.rpcPort = port;
        return this;
    }

    /**
     * 注册服务实现
     * （1）主要用于后期服务调用
     * （2）如何根据 id 获取实现？非常简单，id 是唯一的。
     * 有就是有，没有就抛出异常，直接返回。
     * （3）如果根据 {@link com.github.houbb.rpc.common.rpc.domain.RpcRequest} 获取对应的方法。
     *
     * 3.1 根据 serviceId 获取唯一的实现
     * 3.2 根据 {@link Class#getMethod(String, Class[])} 方法名称+参数类型唯一获取方法
     * 3.3 根据 {@link java.lang.reflect.Method#invoke(Object, Object...)} 执行方法
     *
     * @param serviceId 服务标识
     * @param serviceImpl 服务实现
     * @return this
     * @since 0.0.6
     */
    @Override
    @SuppressWarnings("unchecked")
    public synchronized DefaultServiceRegistry register(final String serviceId, final Object serviceImpl) {
        ArgUtil.notEmpty(serviceId, "serviceId");
        ArgUtil.notNull(serviceImpl, "serviceImpl");

        // 构建对应的其他信息
        ServiceConfig serviceConfig = new DefaultServiceConfig();
        serviceConfig.id(serviceId).reference(serviceImpl);
        serviceConfigList.add(serviceConfig);

        return this;
    }

    @Override
    public ServiceRegistry expose() {
        // 注册所有服务信息
        DefaultServiceFactory.getInstance()
                .registerServices(serviceConfigList);

        // 启动 netty server 信息
        final ChannelHandler channelHandler = ChannelHandlers
                .objectCodecHandler(new RpcServerHandler());
        new DefaultNettyServer().start(rpcPort, channelHandler);

        // 注册到配置中心
        // 初期简单点，直接循环调用即可。
        if(register && CollectionUtil.isNotEmpty(this.registerAddressList)) {
            for(RpcAddress rpcAddress : registerAddressList) {
                ChannelHandler registerHandler = ChannelHandlers.objectCodecHandler(new RpcServerRegisterHandler());
                ChannelFuture channelFuture = DefaultNettyClient.newInstance()
                        .connect(rpcAddress.address(), rpcAddress.port(),
                                registerHandler);

                // 直接写入信息
                ServiceEntry serviceEntry = Service;
                channelFuture.channel().writeAndFlush();
            }
        }

        return this;
    }

    @Override
    public ServiceRegistry registerAddresses(String addresses) {
        this.registerAddressList = RpcAddressBuilder.of(addresses);
        return this;
    }

    @Override
    public ServiceRegistry register(boolean register) {
        this.register = register;
        return this;
    }

}
