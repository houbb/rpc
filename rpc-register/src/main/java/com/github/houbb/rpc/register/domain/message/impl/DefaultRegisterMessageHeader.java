package com.github.houbb.rpc.register.domain.message.impl;

import com.github.houbb.rpc.register.domain.message.RegisterMessageHeader;

/**
 * 注冊信息頭
 * @author binbin.hou
 * @since 0.0.8
 */
class DefaultRegisterMessageHeader implements RegisterMessageHeader {

    /**
     * 消息类型
     * @since 0.0.8
     */
    private int type;

    /**
     * 请求标识
     * @since 0.0.8
     */
    private String seqId;

    @Override
    public int type() {
        return type;
    }

    public DefaultRegisterMessageHeader type(int type) {
        this.type = type;
        return this;
    }

    @Override
    public String seqId() {
        return seqId;
    }

    public DefaultRegisterMessageHeader seqId(String seqId) {
        this.seqId = seqId;
        return this;
    }
}