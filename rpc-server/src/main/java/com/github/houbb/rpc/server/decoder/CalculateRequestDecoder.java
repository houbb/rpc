package com.github.houbb.rpc.server.decoder;

import com.github.houbb.rpc.common.model.CalculateRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 请求参数解码
 * @author binbin.hou
 * @since 0.0.3
 */
public class CalculateRequestDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int one = in.readInt();
        int two = in.readInt();

        CalculateRequest request = new CalculateRequest(one, two);
        out.add(request);
    }

}
