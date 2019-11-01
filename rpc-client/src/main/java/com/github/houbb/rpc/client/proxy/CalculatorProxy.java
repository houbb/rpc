package com.github.houbb.rpc.client.proxy;

import com.github.houbb.rpc.client.core.RpcClient;
import com.github.houbb.rpc.common.model.CalculateRequest;
import com.github.houbb.rpc.common.model.CalculateResponse;
import com.github.houbb.rpc.common.service.Calculator;

/**
 * @author binbin.hou
 * @since 0.0.4
 */
public class CalculatorProxy implements Calculator {

    /**
     * rpc 客户端
     */
    private RpcClient rpcClient;

    /**
     * 创建类
     * （1）默认初始化 client 端
     */
    public CalculatorProxy() {
        rpcClient = new RpcClient();
        rpcClient.start();
    }

    @Override
    public CalculateResponse sum(CalculateRequest request) {
        return rpcClient.calculate(request);
    }

}
