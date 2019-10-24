package com.github.houbb.rpc.register.domain.message.impl;

import com.github.houbb.heaven.util.common.ArgUtil;
import com.github.houbb.rpc.common.support.id.impl.Ids;
import com.github.houbb.rpc.register.domain.message.RegisterMessage;
import com.github.houbb.rpc.register.domain.message.RegisterMessageHeader;

/**
 * 注册消息工具类
 * @author binbin.hou
 * @since 0.0.8
 */
public final class RegisterMessages {

    /**
     * 初始化消息信息
     * @param type 类型
     * @param body 消息体
     * @return 注册消息
     * @since 0.0.8
     */
    public static RegisterMessage of(final int type,
                                     final Object body) {
        String seqId = Ids.id();
        return of(type, seqId, body);
    }

    /**
     * 初始化消息信息
     * @param type 类型
     * @param seqId 消息标识
     * @param body 消息体
     * @return 注册消息
     * @since 0.0.8
     */
    public static RegisterMessage of(final int type,
                                     final String seqId,
                                     final Object body) {
        DefaultRegisterMessage registerMessage = new DefaultRegisterMessage();
        DefaultRegisterMessageHeader messageHeader = new DefaultRegisterMessageHeader();
        messageHeader.type(type);
        messageHeader.seqId(seqId);

        registerMessage.header(messageHeader);
        registerMessage.body(body);
        return registerMessage;
    }

    /**
     * 获取消息的类型
     * @param registerMessage 注册消息
     * @return 消息类型
     * @since 0.0.8
     */
    public static int type(final RegisterMessage registerMessage) {
        RegisterMessageHeader header = header(registerMessage);
        return header.type();
    }

    /**
     * 获取消息唯一标识
     * @param registerMessage 注册消息
     * @return 唯一标识
     * @since 0.0.8
     */
    public static String seqId(final RegisterMessage registerMessage) {
        RegisterMessageHeader header = header(registerMessage);
        return header.seqId();
    }

    /**
     * 获取消息头
     * @param registerMessage 消息
     * @return 消息头
     * @since 0.0.8
     */
    private static RegisterMessageHeader header(final RegisterMessage registerMessage) {
        ArgUtil.notNull(registerMessage, "registerMessage");
        RegisterMessageHeader messageHeader = registerMessage.header();
        ArgUtil.notNull(messageHeader, "messageHeader");
        return messageHeader;
    }

}
