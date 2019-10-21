///*
// * Copyright (c)  2019. houbinbin Inc.
// * rpc All rights reserved.
// */
//
//package com.github.houbb.rpc.test.client;
//
//import com.github.houbb.rpc.client.proxy.CalculatorProxy;
//import com.github.houbb.rpc.common.model.CalculateRequest;
//import com.github.houbb.rpc.common.model.CalculateResponse;
//import com.github.houbb.rpc.common.service.Calculator;
//import org.junit.Ignore;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * rpc 客户端测试代码
// * @author binbin.hou
// * @since 0.0.2
// */
//@Ignore
//public class RpcClientTest {
//
//    /**
//     * 服务启动代码测试
//     * @param args 参数
//     */
//    public static void main(String[] args) {
//        Calculator calculator = new CalculatorProxy();
//
//        CalculateRequest request = new CalculateRequest();
//        request.setOne(5);
//        request.setTwo(6);
//
//        CalculateResponse response = calculator.sum(request);
//        System.out.println("rpc call result: " + response);
//    }
//
//}
