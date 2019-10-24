package com.github.houbb.rpc.server.registry;

/**
 * 服务注册类
 * （1）每个应用唯一
 * （2）每个服务的暴露协议应该保持一致
 * 暂时不提供单个服务的特殊处理，后期可以考虑添加
 *
 * @author binbin.hou
 * @since 0.0.6
 */
public interface ServiceRegistry {

    /**
     * 暴露的 rpc 服务端口信息
     * @param port 端口信息
     * @return this
     * @since 0.0.6
     */
    ServiceRegistry port(final int port);

    /**
     * 注册服务实现
     * @param serviceId 服务标识
     * @param serviceImpl 服务实现
     * @return this
     * @since 0.0.6
     */
    ServiceRegistry register(final String serviceId, final Object serviceImpl);

    /**
     * 暴露所有服务信息
     * （1）本地初始化服务信息
     * （2）启动服务端
     * （3）注册服务到注册中心
     * @return this
     * @since 0.0.6
     */
    ServiceRegistry expose();

    /**
     * 注册中心地址信息
     * @param addresses 地址信息
     * @return this
     * @since 0.0.8
     */
    ServiceRegistry registerAddresses(final String addresses);

    /**
     * 是否注册服务到注册中心
     * @param register 是否注册
     * @return this
     * @since 0.0.8
     */
    ServiceRegistry register(final boolean register);

}
