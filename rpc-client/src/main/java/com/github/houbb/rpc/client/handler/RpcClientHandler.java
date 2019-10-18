/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.client.handler;

import com.github.houbb.json.bs.JsonBs;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.client.core.RpcClient;
import com.github.houbb.rpc.common.model.CalculateResponse;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * <p> 客户端处理类 </p>
 *
 * <pre> Created: 2019/10/16 11:30 下午  </pre>
 * <pre> Project: rpc  </pre>
 *
 * @author houbinbin
 * @since 0.0.2
 */
public class RpcClientHandler extends SimpleChannelInboundHandler {

    private static final Log log = LogFactory.getLog(RpcClient.class);

    /**
     * 响应信息
     * @since 0.0.4
     */
    private CalculateResponse response;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf)msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        String json = new String(bytes);

        log.info("[Client] receive response json: {} ", json);
        this.response = JsonBs.deserialize(json, CalculateResponse.class);
        log.info("[Client] response is :{}", response);
    }

    //TODO: 反序列化时添加 serialId 过滤。
    // JsonBs 添加 bytes 与 object 之间的转换。

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 每次用完要关闭，不然拿不到response，我也不知道为啥（目测得了解netty才行）
        // 个人理解：如果不关闭，则永远会被阻塞。
        ctx.flush();
        ctx.close();
    }

    public CalculateResponse getResponse() {
        return response;
    }

}
