package com.github.houbb.rpc.client.encoder;

import com.github.houbb.rpc.common.model.CalculateRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author binbin.hou
 * @since 0.0.3
 */
public class CalculateRequestEncoder extends MessageToByteEncoder<CalculateRequest> {

    @Override
    protected void encode(ChannelHandlerContext ctx, CalculateRequest msg, ByteBuf out) throws Exception {
        int one = msg.getOne();
        int two = msg.getTwo();

        out.writeInt(one);
        out.writeInt(two);
    }

}
