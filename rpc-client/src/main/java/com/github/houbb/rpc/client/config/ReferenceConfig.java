package com.github.houbb.rpc.client.config;

import com.github.houbb.rpc.common.config.component.Address;

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
public class ReferenceConfig<T> {

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
     * @since 0.0.6
     */
    private List<Address> addresses;

    public String serviceId() {
        return serviceId;
    }

    public ReferenceConfig<T> serviceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public Class<T> serviceInterface() {
        return serviceInterface;
    }

    public ReferenceConfig<T> serviceInterface(Class<T> serviceInterface) {
        this.serviceInterface = serviceInterface;
        return this;
    }

    public List<Address> addresses() {
        return addresses;
    }

    public ReferenceConfig<T> addresses(List<Address> addresses) {
        this.addresses = addresses;
        return this;
    }

    /**
     * 获取对应的引用实现
     * @return 引用代理类
     * @since 0.0.6
     */
    public T reference() {
        // 接口动态代理
        //
        return null;
    }

}
