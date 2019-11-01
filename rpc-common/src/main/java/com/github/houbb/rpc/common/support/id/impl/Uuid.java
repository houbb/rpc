package com.github.houbb.rpc.common.support.id.impl;

import com.github.houbb.heaven.annotation.CommonEager;
import com.github.houbb.heaven.annotation.ThreadSafe;
import com.github.houbb.rpc.common.support.id.Id;

import java.util.UUID;

/**
 * @author binbin.hou
 * @since 0.0.6
 */
@ThreadSafe
@CommonEager
public class Uuid implements Id {

    private Uuid(){}

    private static final Uuid INSTANCE = new Uuid();

    public static Uuid getInstance() {
        return INSTANCE;
    }

    @Override
    public String id() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

}
