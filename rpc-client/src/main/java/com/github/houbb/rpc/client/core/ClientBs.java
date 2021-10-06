/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.client.core;

import com.github.houbb.heaven.util.guava.Guavas;
import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.client.config.reference.ReferenceConfig;
import com.github.houbb.rpc.client.handler.RpcClientHandler;
import com.github.houbb.rpc.client.proxy.ReferenceProxy;
import com.github.houbb.rpc.client.proxy.RemoteInvokeService;
import com.github.houbb.rpc.client.proxy.ServiceContext;
import com.github.houbb.rpc.client.proxy.impl.DefaultReferenceProxy;
import com.github.houbb.rpc.client.proxy.impl.DefaultServiceContext;
import com.github.houbb.rpc.client.proxy.impl.GenericReferenceProxy;
import com.github.houbb.rpc.client.proxy.impl.RemoteInvokeServiceImpl;
import com.github.houbb.rpc.client.support.fail.enums.FailTypeEnum;
import com.github.houbb.rpc.client.support.register.ClientRegisterService;
import com.github.houbb.rpc.client.support.register.impl.ClientRegisterServiceImpl;
import com.github.houbb.rpc.common.config.component.RpcAddress;
import com.github.houbb.rpc.common.config.component.RpcAddressBuilder;
import com.github.houbb.rpc.common.constant.enums.CallTypeEnum;
import com.github.houbb.rpc.common.exception.RpcRuntimeException;
import com.github.houbb.rpc.common.remote.netty.handler.ChannelHandlerFactory;
import com.github.houbb.rpc.common.remote.netty.handler.ChannelHandlers;
import com.github.houbb.rpc.common.rpc.domain.RpcChannelFuture;
import com.github.houbb.rpc.common.support.hook.DefaultShutdownHook;
import com.github.houbb.rpc.common.support.hook.RpcShutdownHook;
import com.github.houbb.rpc.common.support.hook.ShutdownHooks;
import com.github.houbb.rpc.common.support.inteceptor.Interceptor;
import com.github.houbb.rpc.common.support.inteceptor.impl.InterceptorAdaptor;
import com.github.houbb.rpc.common.support.invoke.InvokeManager;
import com.github.houbb.rpc.common.support.invoke.impl.DefaultInvokeManager;
import com.github.houbb.rpc.common.support.resource.ResourceManager;
import com.github.houbb.rpc.common.support.resource.impl.DefaultResourceManager;
import com.github.houbb.rpc.common.support.status.enums.StatusEnum;
import com.github.houbb.rpc.common.support.status.service.StatusManager;
import com.github.houbb.rpc.common.support.status.service.impl.DefaultStatusManager;
import io.netty.channel.ChannelHandler;

import java.util.ArrayList;
import java.util.List;

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
 *
 * @param <T> 接口泛型
 * @author binbin.hou
 * @since 0.0.6
 */
public class ClientBs<T> implements ReferenceConfig<T> {

    /**
     * ClientBs logger
     */
    private static final Log log = LogFactory.getLog(ClientBs.class);

    /**
     * 服务唯一标识
     *
     * @since 0.0.6
     */
    private String serviceId;

    /**
     * 服务接口
     *
     * @since 0.0.6
     */
    private Class<T> serviceInterface;

    /**
     * 服务地址信息
     * （1）如果不为空，则直接根据地址获取
     * （2）如果为空，则采用自动发现的方式
     *
     * 如果为 subscribe 可以自动发现，然后填充这个字段信息。
     *
     * @since 0.0.6
     */
    private List<RpcAddress> rpcAddresses;


    /**
     * 调用超时时间
     *
     * @since 0.0.7
     */
    private long timeout;

    /**
     * 是否进行订阅模式
     *
     * @since 0.0.8
     */
    private boolean subscribe;

    /**
     * 注册中心列表
     *
     * @since 0.0.8
     */
    private List<RpcAddress> registerCenterList;

    /**
     * 调用服务管理类
     *
     * @since 0.0.6
     */
    private InvokeManager invokeManager;

    /**
     * 客户端注册中心服务类
     *
     * @since 0.0.9
     */
    private ClientRegisterService clientRegisterService;

    /**
     * 调用方式
     * @since 0.1.0
     */
    private CallTypeEnum callType;

    /**
     * 失败策略
     * @since 0.1.1
     */
    private FailTypeEnum failType;

    /**
     * 远程调用实现
     * @since 0.1.1
     */
    private RemoteInvokeService remoteInvokeService;

    /**
     * 是否进行泛化调用
     * @since 0.1.2
     */
    private boolean generic;

    /**
     * 拦截器
     * @since 0.1.4
     */
    private Interceptor interceptor;

    /**
     * 状态管理类
     * @since 0.1.3
     */
    private StatusManager statusManager;

    /**
     * 资源管理类
     * @since 0.1.3
     */
    private ResourceManager resourceManager;

    /**
     * 客户端启动检测
     * @since 0.1.5
     */
    private boolean check;

    /**
     * 新建一个客户端实例
     *
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
        // 默认为 60s 超时
        this.timeout = 60 * 1000;
        this.registerCenterList = Guavas.newArrayList();
        this.callType = CallTypeEnum.SYNC;
        this.failType = FailTypeEnum.FAIL_OVER;
        this.generic = false;
        this.check = true;

        // 依赖服务初始化
        this.invokeManager = new DefaultInvokeManager();
        this.clientRegisterService = new ClientRegisterServiceImpl(invokeManager);
        this.remoteInvokeService = new RemoteInvokeServiceImpl();
        this.statusManager = new DefaultStatusManager();
        this.resourceManager = new DefaultResourceManager();
        this.interceptor = new InterceptorAdaptor();
    }

    @Override
    public ClientBs<T> serviceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    @Override
    public ClientBs<T> serviceInterface(Class<T> serviceInterface) {
        this.serviceInterface = serviceInterface;
        return this;
    }

    @Override
    public ReferenceConfig<T> addresses(String addresses) {
        log.info("[Rpc Client] service address set into {} ", addresses);
        this.rpcAddresses = RpcAddressBuilder.of(addresses);
        return this;
    }

    @Override
    public ClientBs<T> check(boolean check) {
        this.check = check;
        return this;
    }

    /**
     * 获取对应的引用实现
     * （1）处理所有的反射代理信息-方法可以抽离，启动各自独立即可。
     * （2）启动对应的长连接
     *
     * @return 引用代理类
     * @since 0.0.6
     */
    @Override
    @SuppressWarnings("unchecked")
    public T reference() {
        // 1. 启动 client 端到 server 端的连接信息
        // 1.1 为了提升性能，可以将所有的 client=>server 的连接都调整为一个 thread。
        // 1.2 初期为了简单，直接使用同步循环的方式。
        // 获取地址列表信息
        List<RpcAddress> rpcAddressList = this.getRpcAddresses();

        //2. 循环链接
        List<RpcChannelFuture> channelFutureList = this.initChannelFutureList(rpcAddressList);

        //2.2 循环将 nettyClient 信息放入到资源列表中
        for(RpcChannelFuture channelFuture : channelFutureList) {
            this.resourceManager.addDestroy(channelFuture.destroyable());
        }

        //3. 生成服务端代理
        ServiceContext<T> proxyContext = buildServiceProxyContext(channelFutureList);
        T reference = null;
        if(!this.generic) {
            ReferenceProxy<T> referenceProxy = new DefaultReferenceProxy<>(proxyContext, remoteInvokeService);
            reference = referenceProxy.proxy();
        } else {
            log.info("[Client] generic reference proxy created.");
            reference = (T) new GenericReferenceProxy(proxyContext, remoteInvokeService);
        }

        //4. 添加客户端钩子
        // 设置状态为可用
        proxyContext.statusManager().status(StatusEnum.ENABLE.code());
        final RpcShutdownHook rpcShutdownHook = new DefaultShutdownHook(statusManager, invokeManager, resourceManager);
        ShutdownHooks.rpcShutdownHook(rpcShutdownHook);

        return reference;
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

    @Override
    public ReferenceConfig<T> callType(CallTypeEnum callTypeEnum) {
        this.callType = callTypeEnum;
        return this;
    }

    @Override
    public ReferenceConfig<T> failType(FailTypeEnum failTypeEnum) {
        this.failType = failTypeEnum;
        return this;
    }

    @Override
    public ClientBs<T> generic(boolean generic) {
        this.generic = generic;
        return this;
    }

    @Override
    public ReferenceConfig<T> interceptor(Interceptor interceptor) {
        this.interceptor = interceptor;
        return this;
    }

    /**
     * 初始化列表
     * @param rpcAddressList 地址
     * @return 结果
     * @since 0.1.6
     */
    private List<RpcChannelFuture> initChannelFutureList(List<RpcAddress> rpcAddressList) {
        // 检测可用性
        if(this.check) {
            //1. 列表为空
            if(CollectionUtil.isEmpty(rpcAddressList)) {
                log.error("[Rpc Client] rpc address list is empty!");
                throw new RpcRuntimeException();
            }

            //2. 初始化
            return ChannelHandlers.channelFutureList(rpcAddressList, new ChannelHandlerFactory() {
                @Override
                public ChannelHandler handler() {
                    final ChannelHandler channelHandler = new RpcClientHandler(invokeManager);
                    return ChannelHandlers.objectCodecHandler(channelHandler);
                }
            });
        }

        // 如果不检测可用性
        List<RpcChannelFuture> resultList = new ArrayList<>();
        if(CollectionUtil.isEmpty(rpcAddressList)) {
            log.warn("[Rpc Client] rpc address list is empty, without check init.");
            return resultList;
        }
        // 如果异常，则捕获
        for(RpcAddress rpcAddress : rpcAddressList) {
            try {
                RpcChannelFuture future = ChannelHandlers.channelFuture(rpcAddress, new ChannelHandlerFactory() {
                    @Override
                    public ChannelHandler handler() {
                        final ChannelHandler channelHandler = new RpcClientHandler(invokeManager);
                        return ChannelHandlers.objectCodecHandler(channelHandler);
                    }
                });

                resultList.add(future);
            } catch (Exception exception) {
                log.error("[Rpc Client] rpc address init failed, without check init. {}", rpcAddress, exception);
            }
        }

        return resultList;
    }

    /**
     * 获取 rpc 地址信息列表
     * （1）默认直接通过指定的地址获取
     * （2）如果指定列表为空，且
     *
     * @return rpc 地址信息列表
     * @since 0.0.8
     */
    private List<RpcAddress> getRpcAddresses() {
        //0. 快速返回
        if (CollectionUtil.isNotEmpty(rpcAddresses)) {
            return rpcAddresses;
        }

        //1. 信息检查
        registerCenterParamCheck();

        //2. 查询服务信息
        return clientRegisterService.queryServerAddressList(serviceId, registerCenterList);
    }

    /**
     * 注册中心参数检查
     * （1）如果可用列表为空，且没有指定自动发现，这个时候服务已经不可用了。
     *
     * @since 0.0.8
     */
    private void registerCenterParamCheck() {
        if (!subscribe) {
            log.error("[Rpc Client] no available services found for serviceId:{}", serviceId);
            throw new RpcRuntimeException();
        }
    }

    /**
     * 构建调用上下文
     *
     * @param channelFutureList 信息列表
     * @return 引用代理上下文
     * @since 0.0.6
     */
    private ServiceContext<T> buildServiceProxyContext(final List<RpcChannelFuture> channelFutureList) {
        DefaultServiceContext<T> serviceContext = new DefaultServiceContext<>();
        serviceContext.serviceId(this.serviceId);
        serviceContext.serviceInterface(this.serviceInterface);
        serviceContext.channelFutures(channelFutureList);
        serviceContext.invokeManager(this.invokeManager);
        serviceContext.timeout(this.timeout);
        serviceContext.callType(this.callType);
        serviceContext.failType(this.failType);
        serviceContext.generic(this.generic);
        serviceContext.statusManager(this.statusManager);
        serviceContext.interceptor(this.interceptor);
        return serviceContext;
    }

}
