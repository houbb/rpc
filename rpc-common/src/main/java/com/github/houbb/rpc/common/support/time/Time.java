package com.github.houbb.rpc.common.support.time;

import com.github.houbb.heaven.annotation.CommonEager;

/**
 * 时间工具类
 * （1）便于后期拓展，可以使用统一的时钟服务。
 * @author binbin.hou
 * @since 0.0.6
 */
@CommonEager
public interface Time {

    /**
     * 获取当前时间
     * @return 当前时间
     * @since 0.0.6
     */
    long time();

}
