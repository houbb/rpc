package com.github.houbb.rpc.client.decoder;

import com.github.houbb.rpc.common.model.CalculateResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 响应参数解码
 * @author binbin.hou
 * @since 0.0.3
 */
public class CalculateResponseDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        boolean success = in.readBoolean();
        int sum = in.readInt();

        CalculateResponse response = new CalculateResponse(success, sum);
        out.add(response);
    }

}
