package com.github.houbb.rpc.common.rpc.domain;

/**
 * 序列化相关处理
 * （1）调用创建时间-createTime
 * （2）调用方式 callType
 * （3）超时时间 timeOut
 *
 * 额外信息：
 * （1）上下文信息
 *
 * @author binbin.hou
 * @since 0.0.6
 */
public interface RpcRequest extends BaseRpc {

    /**
     * 创建时间
     * @return 创建时间
     * @since 0.0.6
     */
    long createTime();

    /**
     * 服务唯一标识
     * @return 服务唯一标识
     * @since 0.0.6
     */
    String serviceId();



    // 调用参数信息列表
    // 调用方法信息

}
