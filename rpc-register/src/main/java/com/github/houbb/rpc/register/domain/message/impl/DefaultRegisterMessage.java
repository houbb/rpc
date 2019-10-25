package com.github.houbb.rpc.register.domain.message.impl;

import com.github.houbb.rpc.register.domain.message.RegisterMessage;
import com.github.houbb.rpc.register.domain.message.RegisterMessageHeader;

/**
 * 默认注册消息
 * @author binbin.hou
 * @since 0.0.8
 */
class DefaultRegisterMessage implements RegisterMessage {

    private static final long serialVersionUID = 3979588494064088927L;

    /**
     * 头信息
     * @since 0.0.8
     */
    private RegisterMessageHeader header;

    /**
     * 消息信息体
     * @since 0.0.8
     */
    private Object body;

    @Override
    public RegisterMessageHeader header() {
        return header;
    }

    public DefaultRegisterMessage header(RegisterMessageHeader header) {
        this.header = header;
        return this;
    }

    @Override
    public Object body() {
        return body;
    }

    public DefaultRegisterMessage body(Object body) {
        this.body = body;
        return this;
    }

    @Override
    public String toString() {
        return "DefaultRegisterMessage{" +
                "header=" + header +
                ", body=" + body +
                '}';
    }

}
