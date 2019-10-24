package com.github.houbb.rpc.register.domain.message;

/**
 * 注册消息体
 * @author binbin.hou
 * @since 0.0.8
 */
public interface RegisterMessage {

    /**
     * 头信息
     * @return 头信息
     * @since 0.0.8
     */
    RegisterMessageHeader header();

    /**
     * 消息信息体
     * @return 消息信息体
     * @since 0.0.8
     */
    Object body();

}
