package com.github.houbb.rpc.common.support.time.impl;

import com.github.houbb.rpc.common.support.time.Time;

/**
 * 时间工具类
 * @author binbin.hou
 * @since 0.0.7
 */
public final class Times {

    private Times(){}

    /**
     * 后续这里将调整为 spi 进行统一处理。
     */
    private static final Time INSTANCE = new DefaultSystemTime();

    /**
     * 获取时间
     * @return 时间
     * @since 0.0.7
     */
    public static long time() {
        return INSTANCE.time();
    }
}
