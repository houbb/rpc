package com.github.houbb.rpc.common.support.inteceptor;

/**
 * rpc 拦截器上下文
 *
 * @author binbin.hou
 * @since 0.1.4
 */
public interface InterceptorContext {

    /**
     * 调用唯一标识
     * @return 唯一标识
     * @since 0.1.4
     */
    String traceId();

    /**
     * 设置调用唯一标识
     * @param traceId 唯一标识
     * @since 0.1.4
     */
    InterceptorContext traceId(String traceId);

    /**
     * 开始时间
     * @return 开始时间
     * @since 0.1.4
     */
    long startTime();

    /**
     * 设置开始时间
     * @param time 时间
     * @return this
     * @since 0.1.4
     */
    InterceptorContext startTime(final long time);

    /**
     * 结束时间
     * @return 结束时间
     * @since 0.1.4
     */
    long endTime();

    /**
     * 设置结束时间
     * @param time 时间
     * @return this
     * @since 0.1.4
     */
    InterceptorContext endTime(final long time);

    /**
     * 设置值
     * @param key key
     * @param value value
     * @return this
     * @since 0.1.4
     */
    InterceptorContext put(final String key, final Object value);

    /**
     * 异常信息
     * @return 异常信息
     * @since 0.1.4
     */
    Throwable error();

    /**
     * 设置异常信息
     * @param error 异常信息
     * @return this
     * @since 0.1.4
     */
    InterceptorContext error(final Throwable error);

    /**
     * 获取对应的值
     * @param key key
     * @return this
     * @since 0.1.4
     */
    Object get(final String key);

    /**
     * 获取对应的值
     * @param key key
     * @param tClass 类型
     * @return this
     * @since 0.1.4
     */
    <T> T get(final String key, final Class<T> tClass);

}
