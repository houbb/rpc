/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.common.util;

import com.github.houbb.heaven.annotation.CommonEager;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * <p> 网络工具类 </p>
 *
 * <pre> Created: 2019/10/24 10:37 下午  </pre>
 * <pre> Project: rpc  </pre>
 *
 * @author houbinbin
 * @since 0.0.8
 */
@CommonEager
public final class NetUtil {

    private NetUtil(){}

    /**
     * 本地服务地址
     * @since 0.0.8
     */
    private static final String LOCAL_HOST;

    static {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        LOCAL_HOST = address.getHostAddress();
    }

    /**
     * 获取本地 ip 地址
     * @return ip 地址
     * @since 0.0.8
     */
    public static String getLocalHost() {
        return LOCAL_HOST;
    }

}
