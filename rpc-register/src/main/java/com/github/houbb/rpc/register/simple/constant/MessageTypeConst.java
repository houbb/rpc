package com.github.houbb.rpc.register.simple.constant;

/**
 * 注册消息枚举
 *
 * （1）后期还可以添加心跳+重连机制。
 *
 * @author binbin.hou
 * @since 0.0.8
 */
public final class MessageTypeConst {

    private MessageTypeConst(){}

    /**
     * 服务端注册
     * @since 0.0.8
     */
    public static final int SERVER_REGISTER = 1;

    /**
     * 服务端注销
     * @since 0.0.8
     */
    public static final int SERVER_UN_REGISTER = 2;

    /**
     * 客户端订阅
     * @since 0.0.8
     */
    public static final int CLIENT_SUBSCRIBE = 3;

    /**
     * 客户端取关
     * @since 0.0.8
     */
    public static final int CLIENT_UN_SUBSCRIBE = 4;

    /**
     * 客户端查询
     * @since 0.0.8
     */
    public static final int CLIENT_LOOK_UP = 5;

    /**
     * 注册中心通知
     * @since 0.0.8
     */
    public static final int REGISTER_NOTIFY = 6;

    /**
     * 注册客户端查询响应
     * @since 0.0.8
     */
    public static final int REGISTER_LOOK_UP_RESP = 7;

}
