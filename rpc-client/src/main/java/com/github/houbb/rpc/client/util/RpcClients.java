///*
// * Copyright (c)  2019. houbinbin Inc.
// * rpc All rights reserved.
// */
//
//package com.github.houbb.rpc.client.util;
//
//import com.github.houbb.heaven.util.guava.Guavas;
//import com.github.houbb.heaven.util.util.CollectionUtil;
//import com.github.houbb.log.integration.core.Log;
//import com.github.houbb.log.integration.core.LogFactory;
//import com.github.houbb.rpc.common.config.component.RpcAddress;
//import com.github.houbb.rpc.common.remote.netty.impl.DefaultNettyClient;
//import com.github.houbb.rpc.common.rpc.domain.RpcChannelFuture;
//import com.github.houbb.rpc.common.rpc.domain.impl.DefaultRpcChannelFuture;
//
//import java.util.List;
//
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelHandler;
//
///**
// * <p> 客户端工具类 </p>
// *
// * <pre> Created: 2019/10/26 10:04 上午  </pre>
// * <pre> Project: rpc  </pre>
// *
// * @author houbinbin
// * @since 0.0.9
// */
//public final class RpcClients {
//
//    private static final Log LOG = LogFactory.getLog(RpcClients.class);
//
//    private RpcClients(){}
//
//    /**
//     * 获取处理后的channel future 列表信息
//     * （1）权重
//     * （2）client 链接信息
//     * （3）地址信息
//     * @param rpcAddressList 地址列表
//     * @param channelHandler 处理信息
//     * @return 信息列表
//     * @since 0.0.9
//     */
//    public static List<RpcChannelFuture> channelFutureList(final List<RpcAddress> rpcAddressList, final ChannelHandler channelHandler) {
//        List<RpcChannelFuture> resultList = Guavas.newArrayList();
//
//        if(CollectionUtil.isNotEmpty(rpcAddressList)) {
//            for(RpcAddress rpcAddress : rpcAddressList) {
//                // 循环中每次都需要一个新的 handler
//                DefaultRpcChannelFuture future = DefaultRpcChannelFuture.newInstance();
//                ChannelFuture channelFuture = DefaultNettyClient.newInstance(rpcAddress.address(), rpcAddress.port(), channelHandler).call();
//
//                future.channelFuture(channelFuture).address(rpcAddress)
//                        .weight(rpcAddress.weight());
//            }
//        }
//
//        return resultList;
//    }
//
//
//}
