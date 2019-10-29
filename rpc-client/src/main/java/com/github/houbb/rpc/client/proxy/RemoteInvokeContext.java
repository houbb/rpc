package com.github.houbb.rpc.client.proxy;

import com.github.houbb.rpc.client.proxy.impl.DefaultRemoteInvokeContext;
import com.github.houbb.rpc.common.rpc.domain.RpcChannelFuture;
import com.github.houbb.rpc.common.rpc.domain.RpcRequest;
import com.github.houbb.rpc.common.rpc.domain.RpcResponse;
import io.netty.channel.Channel;

/**
 * 远程调用上下文
 *
 * 核心目的：
 * （1）用于定义 filter 相关信息
 * （2）用于 load-balance 相关信息处理
 * @param <T> 泛型信息
 * @author binbin.hou
 * @since 0.1.1
 */
public interface RemoteInvokeContext<T> {

    /**
     * 请求信息
     * @return 请求信息
     * @since 0.1.1
     */
    RpcRequest request();

    /**
     * 服务代理上下文信息
     * @return 服务代理信息
     * @since 0.1.1
     */
    ServiceContext<T> serviceProxyContext();

    /**
     * 设置 channel future
     * （1）可以通过 load balance
     * （2）其他各种方式
     * @param channelFuture 消息
     * @return this
     * @since 0.0.9
     */
    RemoteInvokeContext<T> channelFuture(final RpcChannelFuture channelFuture);

    /**
     * 获取 channel future
     * （1）如果不设置，则默认取 {@link #serviceProxyContext()}第一个 channel 信息
     * （2）如果对应信息为空，则直接报错 {@link com.github.houbb.rpc.common.exception.RpcRuntimeException}
     * @return channel 信息
     * @since 0.0.9
     */
    Channel channel();

    /**
     * 请求响应结果
     * @return 请求响应结果
     * @since 0.1.1
     */
    RpcResponse rpcResponse();

    /**
     * 请求响应结果
     * @param rpcResponse 响应结果
     * @return 请求响应结果
     * @since 0.1.1
     */
    DefaultRemoteInvokeContext<T> rpcResponse(final RpcResponse rpcResponse);

    /**
     * 获取重试次数
     * @return 重试次数
     */
    int retryTimes();

    /**
     * 设置重试次数
     * @param retryTimes 设置重试次数
     * @return this
     */
    DefaultRemoteInvokeContext<T> retryTimes(final int retryTimes);

    /**
     * 在整个调用生命周期中唯一的标识号
     * （1）重试也不会改变
     * （2）只在第一次调用的时候进行设置。
     * @return 订单号
     * @since 0.1.1
     */
    String traceId();

    /**
     * 远程调用服务信息
     * @return 远程调用服务信息
     * @since 0.1.1
     */
    RemoteInvokeService remoteInvokeService();

}
