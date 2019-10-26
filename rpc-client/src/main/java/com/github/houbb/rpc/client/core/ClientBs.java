/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.client.core;

import com.github.houbb.heaven.support.handler.IHandler;
import com.github.houbb.heaven.util.guava.Guavas;
import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.client.config.reference.ReferenceConfig;
import com.github.houbb.rpc.client.handler.RpcClientHandler;
import com.github.houbb.rpc.client.handler.RpcClientRegisterHandler;
import com.github.houbb.rpc.client.invoke.InvokeService;
import com.github.houbb.rpc.client.invoke.impl.DefaultInvokeService;
import com.github.houbb.rpc.client.proxy.ReferenceProxy;
import com.github.houbb.rpc.client.proxy.context.ProxyContext;
import com.github.houbb.rpc.client.proxy.context.impl.DefaultProxyContext;
import com.github.houbb.rpc.common.config.component.RpcAddress;
import com.github.houbb.rpc.common.config.component.RpcAddressBuilder;
import com.github.houbb.rpc.common.exception.RpcRuntimeException;
import com.github.houbb.rpc.common.remote.netty.handler.ChannelHandlers;
import com.github.houbb.rpc.common.remote.netty.impl.DefaultNettyClient;
import com.github.houbb.rpc.common.rpc.domain.RpcChannelFuture;
import com.github.houbb.rpc.common.rpc.domain.RpcResponse;
import com.github.houbb.rpc.common.rpc.domain.impl.DefaultRpcChannelFuture;
import com.github.houbb.rpc.common.rpc.domain.impl.RpcResponses;
import com.github.houbb.rpc.register.domain.entry.ServiceEntry;
import com.github.houbb.rpc.register.domain.entry.impl.ServiceEntryBuilder;
import com.github.houbb.rpc.register.domain.message.RegisterMessage;
import com.github.houbb.rpc.register.domain.message.impl.RegisterMessages;
import com.github.houbb.rpc.register.simple.constant.MessageTypeConst;

import java.util.List;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;

/**
 * 引用配置类
 *
 * 后期配置：
 * （1）timeout 调用超时时间
 * （2）version 服务版本处理
 * （3）callType 调用方式 oneWay/sync/async
 * （4）check 是否必须要求服务启动。
 *
 * spi:
 * （1）codec 序列化方式
 * （2）netty 网络通讯架构
 * （3）load-balance 负载均衡
 * （4）失败策略 fail-over/fail-fast
 *
 * filter:
 * （1）路由
 * （2）耗时统计 monitor 服务治理
 *
 * 优化思考：
 * （1）对于唯一的 serviceId，其实其 interface 是固定的，是否可以省去？
 * @author binbin.hou
 * @since 0.0.6
 * @param <T> 接口泛型
 */
public class ClientBs<T> implements ReferenceConfig<T> {

    private static final Log LOG = LogFactory.getLog(ClientBs.class);

    /**
     * 服务唯一标识
     * @since 0.0.6
     */
    private String serviceId;

    /**
     * 服务接口
     * @since 0.0.6
     */
    private Class<T> serviceInterface;

    /**
     * 服务地址信息
     * （1）如果不为空，则直接根据地址获取
     * （2）如果为空，则采用自动发现的方式
     *
     * 如果为 subscribe 可以自动发现，然后填充这个字段信息。
     * @since 0.0.6
     */
    private List<RpcAddress> rpcAddresses;

    /**
     * 用于写入信息
     * （1）client 连接 server 端的 channel future
     * （2）后期进行 Load-balance 路由等操作。可以放在这里执行。
     * @since 0.0.6
     */
    @Deprecated
    private List<ChannelFuture> channelFutures;

    /**
     * 调用服务管理类
     * @since 0.0.6
     */
    private InvokeService invokeService;

    /**
     * 调用超时时间
     * @since 0.0.7
     */
    private long timeout;

    /**
     * 是否进行订阅模式
     * @since 0.0.8
     */
    private boolean subscribe;

    /**
     * 注册中心列表
     * @since 0.0.8
     */
    private List<RpcAddress> registerCenterList;

    /**
     * 注册中心超时时间
     * @since 0.0.8
     */
    private long registerCenterTimeOut;

    /**
     * 新建一个客户端实例
     * @param <T> 泛型
     * @return this
     * @since 0.0.9
     */
    public static <T> ClientBs<T> newInstance() {
        return new ClientBs<>();
    }

    private ClientBs() {
        // 初始化信息
        this.rpcAddresses = Guavas.newArrayList();
        this.channelFutures = Guavas.newArrayList();
        this.invokeService = new DefaultInvokeService();
        // 默认为 60s 超时
        this.timeout = 60*1000;
        this.registerCenterList = Guavas.newArrayList();
        this.registerCenterTimeOut = 60*1000;
    }

    @Override
    public String serviceId() {
        return serviceId;
    }

    @Override
    public ClientBs<T> serviceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    @Override
    public Class<T> serviceInterface() {
        return serviceInterface;
    }

    @Override
    public ClientBs<T> serviceInterface(Class<T> serviceInterface) {
        this.serviceInterface = serviceInterface;
        return this;
    }

    @Override
    public ReferenceConfig<T> addresses(String addresses) {
        LOG.info("[Rpc Client] service address set into {} ", addresses);
        this.rpcAddresses = RpcAddressBuilder.of(addresses);
        return this;
    }

    /**
     * 获取对应的引用实现
     * （1）处理所有的反射代理信息-方法可以抽离，启动各自独立即可。
     * （2）启动对应的长连接
     * @return 引用代理类
     * @since 0.0.6
     */
    @Override
    public T reference() {
        // 1. 启动 client 端到 server 端的连接信息
        // 1.1 为了提升性能，可以将所有的 client=>server 的连接都调整为一个 thread。
        // 1.2 初期为了简单，直接使用同步循环的方式。
        // 获取地址列表信息
        List<RpcAddress> rpcAddressList = getRpcAddresses();

        //2. 循环链接
        List<RpcChannelFuture> channelFutureList = channelFutureList(rpcAddressList);

        //3. 接口动态代理
        ProxyContext<T> proxyContext = buildProxyContext(channelFutureList);
        //3.1 动态代理
        //3.2 为了提升性能，可以使用 javaassit 等基于字节码的技术
        return ReferenceProxy.newProxyInstance(proxyContext);
    }



    @Override
    public ClientBs<T> timeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    @Override
    public ReferenceConfig<T> subscribe(boolean subscribe) {
        this.subscribe = subscribe;
        return this;
    }

    @Override
    public ReferenceConfig<T> registerCenter(String addresses) {
        this.registerCenterList = RpcAddressBuilder.of(addresses);
        return this;
    }

    /**
     * 获取 rpc 地址信息列表
     * （1）默认直接通过指定的地址获取
     * （2）如果指定列表为空，且
     * @return rpc 地址信息列表
     * @since 0.0.8
     */
    private List<RpcAddress> getRpcAddresses() {
        //0. 快速返回
        if(CollectionUtil.isNotEmpty(rpcAddresses)) {
            return rpcAddresses;
        }

        //1. 信息检查
        registerCenterParamCheck();

        //2. 查询服务信息
        List<ServiceEntry> serviceEntries = lookUpServiceEntryList();
        LOG.info("[Client] register center serviceEntries: {}", serviceEntries);
        //3. 结果转换
        return CollectionUtil.toList(serviceEntries, new IHandler<ServiceEntry, RpcAddress>() {
            @Override
            public RpcAddress handle(ServiceEntry serviceEntry) {
                return new RpcAddress(serviceEntry.ip(),
                        serviceEntry.port(), serviceEntry.weight());
            }
        });
    }

    /**
     * 注册中心参数检查
     * （1）如果可用列表为空，且没有指定自动发现，这个时候服务已经不可用了。
     * @since 0.0.8
     */
    private void registerCenterParamCheck() {
        if(!subscribe) {
            LOG.error("[Rpc Client] no available services found for serviceId:{}",
                    serviceId);
            throw new RpcRuntimeException();
        }
        if(CollectionUtil.isEmpty(registerCenterList)) {
            LOG.error("[Rpc Client] register center address can't be null!:{}",
                    serviceId);
            throw new RpcRuntimeException();
        }
    }

    /**
     * 查询服务信息列表
     * @return 服务明细列表
     * @since 0.0.8
     */
    @SuppressWarnings("unchecked")
    private List<ServiceEntry> lookUpServiceEntryList() {
        //1. 连接到注册中心
        List<ChannelFuture> channelFutureList = connectRegisterCenter();

        //2. 选择一个
        // 直接取第一个即可，后续可以使用 load-balance 策略。
        ChannelFuture channelFuture = channelFutureList.get(0);

        //3. 发送查询请求
        ServiceEntry serviceEntry = ServiceEntryBuilder.of(serviceId);
        RegisterMessage registerMessage = RegisterMessages.of(MessageTypeConst.CLIENT_LOOK_UP, serviceEntry);
        final String seqId = registerMessage.seqId();
        invokeService.addRequest(seqId, registerCenterTimeOut);
        channelFuture.channel().writeAndFlush(registerMessage);

        //4. 等待查询结果
        RpcResponse rpcResponse = invokeService.getResponse(seqId);
        return (List<ServiceEntry>) RpcResponses.getResult(rpcResponse);
    }

    /**
     * 连接到注册中心
     * @return 对应的结果列表
     * @since 0.0.8
     */
    private List<ChannelFuture> connectRegisterCenter() {
        List<ChannelFuture> futureList = Guavas.newArrayList(registerCenterList.size());
        ChannelHandler channelHandler = ChannelHandlers.objectCodecHandler(new RpcClientRegisterHandler(invokeService));

        for(RpcAddress rpcAddress : registerCenterList) {
            final String ip = rpcAddress.address();
            final int port = rpcAddress.port();
            LOG.info("[Rpc Client] connect to register {}:{} ",
                    ip, port);
            ChannelFuture channelFuture = DefaultNettyClient
                    .newInstance(ip, port, channelHandler)
                    .call();

            futureList.add(channelFuture);
        }
        return futureList;
    }


    /**
     * 构建调用上下文
     * @param channelFutureList 信息列表
     * @return 引用代理上下文
     * @since 0.0.6
     */
    private ProxyContext<T> buildProxyContext(final List<RpcChannelFuture> channelFutureList) {
        DefaultProxyContext<T> proxyContext = new DefaultProxyContext<>();
        proxyContext.serviceId(this.serviceId);
        proxyContext.serviceInterface(this.serviceInterface);
        proxyContext.channelFutures(channelFutureList);
        proxyContext.invokeService(this.invokeService);
        proxyContext.timeout(this.timeout);
        return proxyContext;
    }

    /**
     * 获取处理后的channel future 列表信息
     * （1）权重
     * （2）client 链接信息
     * （3）地址信息
     * @param rpcAddressList 地址信息列表
     * @return 信息列表
     * @since 0.0.9
     */
    private List<RpcChannelFuture> channelFutureList(final List<RpcAddress> rpcAddressList) {
        List<RpcChannelFuture> resultList = Guavas.newArrayList();

        if(CollectionUtil.isNotEmpty(rpcAddressList)) {
            for(RpcAddress rpcAddress : rpcAddressList) {
                final ChannelHandler channelHandler = new RpcClientHandler(invokeService);
                final ChannelHandler actualChannlHandler = ChannelHandlers.objectCodecHandler(channelHandler);

                // 循环中每次都需要一个新的 handler
                DefaultRpcChannelFuture future = DefaultRpcChannelFuture.newInstance();
                ChannelFuture channelFuture = DefaultNettyClient.newInstance(rpcAddress.address(), rpcAddress.port(), actualChannlHandler).call();

                future.channelFuture(channelFuture).address(rpcAddress)
                        .weight(rpcAddress.weight());
                resultList.add(future);
            }
        }

        return resultList;
    }

}
