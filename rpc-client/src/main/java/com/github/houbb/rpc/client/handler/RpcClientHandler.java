/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.client.handler;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.client.core.RpcClient;
import com.github.houbb.rpc.common.model.CalculateRequest;
import com.github.houbb.rpc.common.model.CalculateResponse;
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

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        CalculateRequest request = new CalculateRequest(1, 2);

        ctx.writeAndFlush(request);
        log.info("[Client] request is :{}", request);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        CalculateResponse response = (CalculateResponse)msg;
        log.info("[Client] response is :{}", response);
    }

}
