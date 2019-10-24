/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.common.util;

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
public final class NetUtil {

    /**
     * 获取本地 ip 地址
     * @return ip 地址
     * @since 0.0.8
     */
    public static String getLocalHost() {
        try {
            InetAddress address = InetAddress.getLocalHost();

            return address.getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println(getLocalHost());
    }

}
