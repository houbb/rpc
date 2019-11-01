package com.github.houbb.rpc.common.support.id.impl;

import com.github.houbb.heaven.annotation.CommonEager;

/**
 * 标识工具类
 * @author binbin.hou
 * @since 0.0.8
 */
@CommonEager
public final class Ids {

    private Ids() {
    }

    public static String id() {
        return Uuid.getInstance().id();
    }

}
