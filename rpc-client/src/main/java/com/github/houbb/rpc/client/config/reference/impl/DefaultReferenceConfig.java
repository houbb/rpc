package com.github.houbb.rpc.client.config.reference.impl;

import com.github.houbb.heaven.constant.PunctuationConst;
import com.github.houbb.heaven.util.common.ArgUtil;
import com.github.houbb.heaven.util.guava.Guavas;
import com.github.houbb.heaven.util.lang.NumUtil;
import com.github.houbb.rpc.client.config.reference.ReferenceConfig;
import com.github.houbb.rpc.client.core.RpcClient;
import com.github.houbb.rpc.client.core.context.impl.DefaultRpcClientContext;
import com.github.houbb.rpc.client.handler.RpcClientHandler;
import com.github.houbb.rpc.client.invoke.InvokeService;
import com.github.houbb.rpc.client.invoke.impl.DefaultInvokeService;
import com.github.houbb.rpc.client.proxy.ReferenceProxy;
import com.github.houbb.rpc.client.proxy.context.ProxyContext;
import com.github.houbb.rpc.client.proxy.context.impl.DefaultProxyContext;
import com.github.houbb.rpc.common.config.component.RpcAddress;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;

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
 * @author binbin.hou
 * @since 0.0.6
 * @param <T> 接口泛型
 */
public class DefaultReferenceConfig<T> implements ReferenceConfig<T> {

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
     * TODO: 这里调整为 set 更加合理。
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

    public DefaultReferenceConfig() {
        // 初始化信息
        this.rpcAddresses = Guavas.newArrayList();
        this.channelFutures = Guavas.newArrayList();
        this.invokeService = new DefaultInvokeService();
        // 默认为 60s 超时
        this.timeout = 60*1000;
    }

    @Override
    public String serviceId() {
        return serviceId;
    }

    @Override
    public DefaultReferenceConfig<T> serviceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    @Override
    public Class<T> serviceInterface() {
        return serviceInterface;
    }

    @Override
    public DefaultReferenceConfig<T> serviceInterface(Class<T> serviceInterface) {
        this.serviceInterface = serviceInterface;
        return this;
    }

    @Override
    public ReferenceConfig<T> addresses(String addresses) {
        ArgUtil.notEmpty(addresses, "addresses");

        String[] addressArray = addresses.split(PunctuationConst.COMMA);
        ArgUtil.notEmpty(addressArray, "addresses");

        for(String address : addressArray) {
            String[] addressSplits = address.split(PunctuationConst.COLON);
            if(addressSplits.length < 2) {
                throw new IllegalArgumentException("Address must be has ip and port, like 127.0.0.1:9527");
            }
            String ip = addressSplits[0];
            int port = NumUtil.toIntegerThrows(addressSplits[1]);
            // 包含权重信息
            int weight = 1;
            if(addressSplits.length >= 3) {
                weight = NumUtil.toInteger(addressSplits[2], 1);
            }

            RpcAddress rpcAddress = new RpcAddress(ip, port, weight);
            this.rpcAddresses.add(rpcAddress);
        }

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
        // 循环连接
        for(RpcAddress rpcAddress : rpcAddresses) {
            final ChannelHandler channelHandler = new RpcClientHandler(invokeService);
            final DefaultRpcClientContext context = new DefaultRpcClientContext();
            context.address(rpcAddress.address()).port(rpcAddress.port()).channelHandler(channelHandler);
            ChannelFuture channelFuture = new RpcClient(context).connect();
            // 循环同步等待
            // 如果出现异常，直接中断？捕获异常继续进行？？
            channelFutures.add(channelFuture);
        }

        // 2. 接口动态代理
        ProxyContext<T> proxyContext = buildReferenceProxyContext();
        return ReferenceProxy.newProxyInstance(proxyContext);
    }

    /**
     * 构建调用上下文
     * @return 引用代理上下文
     * @since 0.0.6
     */
    private ProxyContext<T> buildReferenceProxyContext() {
        DefaultProxyContext<T> proxyContext = new DefaultProxyContext<>();
        proxyContext.serviceId(this.serviceId);
        proxyContext.serviceInterface(this.serviceInterface);
        proxyContext.channelFutures(this.channelFutures);
        proxyContext.invokeService(this.invokeService);
        proxyContext.timeout(this.timeout);
        return proxyContext;
    }

    @Override
    public DefaultReferenceConfig<T> timeout(long timeout) {
        this.timeout = timeout;
        return this;
    }
}
