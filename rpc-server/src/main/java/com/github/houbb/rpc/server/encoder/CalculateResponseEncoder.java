package com.github.houbb.rpc.server.encoder;

import com.github.houbb.rpc.common.model.CalculateResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author binbin.hou
 * @since 0.0.3
 */
@Deprecated
public class CalculateResponseEncoder extends MessageToByteEncoder<CalculateResponse> {

    @Override
    protected void encode(ChannelHandlerContext ctx, CalculateResponse msg, ByteBuf out) throws Exception {
        boolean success = msg.isSuccess();
        int result = msg.getSum();
        out.writeBoolean(success);
        out.writeInt(result);
    }

}
