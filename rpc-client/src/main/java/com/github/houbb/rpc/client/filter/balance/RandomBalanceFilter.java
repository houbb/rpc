package com.github.houbb.rpc.client.filter.balance;

import com.github.houbb.heaven.annotation.ThreadSafe;
import com.github.houbb.rpc.common.rpc.domain.RpcChannelFuture;
import com.github.houbb.rpc.common.rpc.filter.RpcFilter;
import com.github.houbb.rpc.common.rpc.filter.RpcFilterContext;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机负载均衡过滤器
 *
 * 参考：https://www.cnblogs.com/xwdreamer/archive/2012/06/13/2547426.html
 * @author binbin.hou
 * @since 0.0.9
 */
@ThreadSafe
public class RandomBalanceFilter implements RpcFilter {

    @Override
    public void filter(RpcFilterContext rpcFilterContext) {
        List<RpcChannelFuture> channelFutures = rpcFilterContext.channelFutures();
        final int size = channelFutures.size();
        Random random = ThreadLocalRandom.current();
        int index = random.nextInt(size-1);
        rpcFilterContext.channelFuture(channelFutures.get(index));
    }

}
