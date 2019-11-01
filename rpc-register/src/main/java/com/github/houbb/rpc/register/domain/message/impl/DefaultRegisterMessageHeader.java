package com.github.houbb.rpc.register.domain.message.impl;

import com.github.houbb.rpc.register.domain.message.RegisterMessageHeader;

/**
 * 注冊信息頭
 * @author binbin.hou
 * @since 0.0.8
 */
class DefaultRegisterMessageHeader implements RegisterMessageHeader {

    private static final long serialVersionUID = -5742810870688287022L;

    /**
     * 消息类型
     * @since 0.0.8
     */
    private int type;

    @Override
    public int type() {
        return type;
    }

    public DefaultRegisterMessageHeader type(int type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return "DefaultRegisterMessageHeader{" +
                "type=" + type +
                '}';
    }

}
