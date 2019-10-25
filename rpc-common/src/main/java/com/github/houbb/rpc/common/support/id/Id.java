package com.github.houbb.rpc.common.support.id;

import com.github.houbb.heaven.annotation.CommonEager;

/**
 * 唯一标识
 * @author binbin.hou
 * @since 0.0.6
 */
@CommonEager
public interface Id {

    /**
     * 获取唯一标识信息
     * @return 唯一标识
     * @since 0.0.6
     */
    String id();

}
