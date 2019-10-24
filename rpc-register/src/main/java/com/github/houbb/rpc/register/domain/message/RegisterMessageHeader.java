package com.github.houbb.rpc.register.domain.message;

import com.github.houbb.rpc.register.simple.constant.MessageTypeConst;

/**
 * 注冊信息頭
 * @author binbin.hou
 * @since 0.0.8
 */
public interface RegisterMessageHeader {

    /**
     * 消息类型
     * @return 消息类型
     * @since 0.0.8
     * @see MessageTypeConst 类型常量
     */
    int type();

    /**
     * 请求唯一标识
     * @return 唯一标识
     * @since 0.0.8
     */
    String seqId();

}
