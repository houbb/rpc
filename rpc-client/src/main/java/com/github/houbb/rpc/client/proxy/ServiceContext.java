package com.github.houbb.rpc.client.proxy;

import com.github.houbb.rpc.client.support.fail.enums.FailTypeEnum;
import com.github.houbb.rpc.common.constant.enums.CallTypeEnum;
import com.github.houbb.rpc.common.rpc.domain.RpcChannelFuture;
import com.github.houbb.rpc.common.support.invoke.InvokeManager;
import com.github.houbb.rpc.common.support.status.service.StatusManager;

import java.util.List;

/**
 * 反射调用上下文
 * @author binbin.hou
 * @since 0.0.6
 * @see com.github.houbb.rpc.client.config.reference.ReferenceConfig 对这里的信息进行一次转换。
 */
public interface ServiceContext<T> {

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
     * 调用服务
     * @return 调用服务
     * @since 0.0.6
     */
    InvokeManager invokeService();

    /**
     * 超时时间
     * 单位：mills
     * @return 超时时间
     * @since 0.0.7
     */
    long timeout();

    /**
     * netty channel 信息
     * @return channel 信息
     * @since 0.0.9
     */
    List<RpcChannelFuture> channelFutures();

    /**
     * 调用方式
     * @return 枚举值
     * @since 0.1.0
     */
    CallTypeEnum callType();

    /**
     * 失败策略
     * @return 失败策略枚举
     * @since 0.1.1
     */
    FailTypeEnum failType();

    /**
     * 是否进行泛化调用
     * @return 是否
     * @since 0.1.2
     */
    boolean generic();

    /**
     * 状态管理类
     * @return 状态管理类
     * @since 0.1.3
     */
    StatusManager statusManager();

}
