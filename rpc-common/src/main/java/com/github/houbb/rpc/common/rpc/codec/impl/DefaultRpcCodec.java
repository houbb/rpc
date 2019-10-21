package com.github.houbb.rpc.common.rpc.codec.impl;

import com.github.houbb.json.bs.JsonBs;
import com.github.houbb.rpc.common.rpc.codec.RpcCodec;

/**
 * 默认序列化相关处理
 * @author binbin.hou
 * @since 0.0.6
 */
public class DefaultRpcCodec implements RpcCodec {

    @Override
    public byte[] toBytes(Object object) {
        return JsonBs.serializeBytes(object);
    }

    @Override
    public <T> T toObject(byte[] bytes, Class<T> tClass) {
        return JsonBs.deserializeBytes(bytes, tClass);
    }

}
