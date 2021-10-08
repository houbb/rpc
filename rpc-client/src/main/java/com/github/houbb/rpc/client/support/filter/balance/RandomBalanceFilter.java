package com.github.houbb.rpc.client.support.filter.balance;

import com.github.houbb.heaven.annotation.ThreadSafe;
import com.github.houbb.rpc.client.proxy.RemoteInvokeContext;
import com.github.houbb.rpc.client.proxy.ServiceContext;
import com.github.houbb.rpc.client.support.filter.RpcFilter;
import com.github.houbb.rpc.client.support.register.ClientRegisterManager;
import com.github.houbb.rpc.common.rpc.domain.RpcChannelFuture;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机负载均衡过滤器
 * 参考：https://www.cnblogs.com/xwdreamer/archive/2012/06/13/2547426.html
 *
 * @author binbin.hou
 * @since 0.0.9
 */
@ThreadSafe
@Deprecated
public class RandomBalanceFilter implements RpcFilter {

    @Override
    @SuppressWarnings("all")
    public void filter(RemoteInvokeContext context) {
        final ServiceContext serviceContext = context.serviceProxyContext();
        final ClientRegisterManager clientRegisterManager = serviceContext.clientRegisterManager();
        final String serviceId = serviceContext.serviceId();
        List<RpcChannelFuture> channelFutures = clientRegisterManager.queryServerChannelFutures(serviceId);
        final int size = channelFutures.size();

        Random random = ThreadLocalRandom.current();
        int index = random.nextInt(size);

        context.channelFuture(channelFutures.get(index));
    }

}
