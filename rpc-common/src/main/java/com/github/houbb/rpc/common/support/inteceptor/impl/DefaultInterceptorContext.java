package com.github.houbb.rpc.common.support.inteceptor.impl;

import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.rpc.common.support.inteceptor.InterceptorContext;

import java.util.HashMap;
import java.util.Map;

/**
 * rpc 拦截器上下文
 *
 * @author binbin.hou
 * @since 0.1.4
 */
public class DefaultInterceptorContext implements InterceptorContext {

    /**
     * 唯一标识
     * @since 0.1.4
     */
    private String traceId;

    /**
     * 开始时间
     * @since 0.1.4
     */
    private long startTime;

    /**
     * 结束时间
     * @since 0.1.4
     */
    private long endTime;

    /**
     * map 集合
     * @since 0.1.4
     */
    private Map<String, Object> map;

    /**
     * 异常信息
     * @since 0.1.4
     */
    private Throwable error;

    private DefaultInterceptorContext() {
        map = new HashMap<>();
    }

    /**
     * 创建一个新的对象实例
     * @return this
     * @since 0.1.4
     */
    public static DefaultInterceptorContext newInstance() {
        return new DefaultInterceptorContext();
    }

    @Override
    public String traceId() {
        return traceId;
    }

    @Override
    public DefaultInterceptorContext traceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    @Override
    public long startTime() {
        return startTime;
    }

    @Override
    public DefaultInterceptorContext startTime(long startTime) {
        this.startTime = startTime;
        return this;
    }

    @Override
    public long endTime() {
        return endTime;
    }

    @Override
    public DefaultInterceptorContext endTime(long endTime) {
        this.endTime = endTime;
        return this;
    }

    @Override
    public InterceptorContext put(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

    @Override
    public Object get(String key) {
        return this.map.get(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> tClass) {
        Object object = this.get(key);
        if(ObjectUtil.isNotNull(object)) {
            return (T)object;
        }
        return null;
    }

    @Override
    public Throwable error() {
        return error;
    }

    @Override
    public DefaultInterceptorContext error(Throwable error) {
        this.error = error;
        return this;
    }

    @Override
    public String toString() {
        return "DefaultInterceptorContext{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", map=" + map +
                ", error=" + error +
                '}';
    }

}
