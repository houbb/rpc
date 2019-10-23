package com.github.houbb.rpc.client.proxy.context;

import com.github.houbb.rpc.client.handler.RpcClientHandler;
import com.github.houbb.rpc.client.invoke.InvokeService;
import io.netty.channel.ChannelFuture;

import java.util.List;

/**
 * 反射调用上下文
 * @author binbin.hou
 * @since 0.0.6
 * @see com.github.houbb.rpc.client.config.reference.ReferenceConfig 对这里的信息进行一次转换。
 */
public interface ProxyContext<T> {

    /**
     * 服务唯一标识
     * @since 0.0.6
     * @return 服务唯一标识
     */
    String serviceId();

    /**
     * 服务接口
     * @since 0.0.6
     * @return 服务接口
     */
    Class<T> serviceInterface();

    /**
     * netty channel 信息
     * @return channel 信息
     * @since 0.0.6
     */
    List<ChannelFuture> channelFutures();

    /**
     * 调用服务
     * @return 调用服务
     * @since 0.0.6
     */
    InvokeService invokeService();
}
