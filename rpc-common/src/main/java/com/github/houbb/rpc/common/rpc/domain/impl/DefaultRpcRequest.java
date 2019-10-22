package com.github.houbb.rpc.common.rpc.domain.impl;

import com.github.houbb.rpc.common.rpc.domain.RpcRequest;

import java.util.List;

/**
 * 默认 rpc 入参
 * @author binbin.hou
 * @since 0.0.6
 * @see Class#getMethod(String, Class[]) 反射获取方法信息
 * @see java.lang.reflect.Method#invoke(Object, Object...) 方法反射调用
 */
public class DefaultRpcRequest implements RpcRequest {

    private static final long serialVersionUID = 4284511516221766313L;

    /**
     * 唯一标识号
     * （）
     * @since 0.0.6
     */
    private String seqId;

    /**
     * 创建时间
     * @since 0.0.6
     * @see System#currentTimeMillis() 当前时间
     */
    private long createTime;

    @Override
    public String seqId() {
        return seqId;
    }

    @Override
    public DefaultRpcRequest seqId(String seqId) {
        this.seqId = seqId;
        return this;
    }

    @Override
    public long createTime() {
        return createTime;
    }

    @Override
    public String serviceId() {
        return null;
    }

    @Override
    public String methodName() {
        return null;
    }

    @Override
    public List<String> paramTypeNames() {
        return null;
    }

    @Override
    public Object[] paramValues() {
        return new Object[0];
    }

    public DefaultRpcRequest createTime(long createTime) {
        this.createTime = createTime;
        return this;
    }
}
