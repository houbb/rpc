/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.register.simple.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * <p> 注册中心服务器处理类 </p>
 *
 * <pre> Created: 2019/10/23 10:29 下午  </pre>
 * <pre> Project: rpc  </pre>
 *
 * 请求的标准化：
 * （1）对于 server 的服务注册，client 的配置拉取。
 * 二者都是将 register 作为服务端。所以需要统一请求信息。
 *（2）对于 server 的注册，不需要提供对应的反馈信息
 *（3）当配置发生变化时，需要及时通知所有的 client 端。
 * 这里就需要知道哪些是客户端？？
 *
 * @author houbinbin
 * @since 0.0.8
 */
public class SimpleRegisterServerHandler extends SimpleChannelInboundHandler {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 这里和 rpc 调用一样。
        // 可以对当前信息进行抽象化。
        // 甚至不需要，直接调用即可。
    }

}
