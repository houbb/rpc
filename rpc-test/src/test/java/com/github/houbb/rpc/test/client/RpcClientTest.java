/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.test.client;

import com.github.houbb.rpc.client.core.RpcClient;
import com.github.houbb.rpc.server.core.RpcServer;

import org.junit.Ignore;

/**
 * rpc 客户端测试代码
 * @author binbin.hou
 * @since 0.0.2
 */
@Ignore
public class RpcClientTest {

    /**
     * 服务启动代码测试
     * @param args 参数
     */
    public static void main(String[] args) {
        new RpcClient().start();
    }

}
