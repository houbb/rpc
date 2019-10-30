package com.github.houbb.rpc.client.proxy.impl;

import com.github.houbb.heaven.util.id.impl.Ids;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.client.proxy.RemoteInvokeContext;
import com.github.houbb.rpc.client.proxy.RemoteInvokeService;
import com.github.houbb.rpc.client.proxy.ServiceContext;
import com.github.houbb.rpc.client.support.calltype.CallTypeStrategy;
import com.github.houbb.rpc.client.support.calltype.impl.CallTypeStrategyFactory;
import com.github.houbb.rpc.client.support.fail.FailStrategy;
import com.github.houbb.rpc.client.support.fail.impl.FailStrategyFactory;
import com.github.houbb.rpc.client.support.filter.RpcFilter;
import com.github.houbb.rpc.client.support.filter.balance.RandomBalanceFilter;
import com.github.houbb.rpc.common.rpc.domain.RpcRequest;
import com.github.houbb.rpc.common.rpc.domain.RpcResponse;
import com.github.houbb.rpc.common.support.invoke.InvokeManager;
import io.netty.channel.Channel;

/**
 * 远程调用实现
 * @author binbin.hou
 * @since 0.1.1
 */
public class RemoteInvokeServiceImpl implements RemoteInvokeService {

    private static final Log LOG = LogFactory.getLog(RemoteInvokeServiceImpl.class);

    @Override
    public Object remoteInvoke(RemoteInvokeContext context) {
        // 设置当前调用的唯一标识
        final RpcRequest rpcRequest = context.request();
        final ServiceContext proxyContext = context.serviceProxyContext();
        final String seqId = Ids.uuid32();
        rpcRequest.seqId(seqId);

        // 这里使用 load-balance 进行选择 channel 写入。
        // 构建 filter 相关信息,结合 pipeline 进行整合
        this.doFilter(context);
        final Channel channel = context.channel();
        LOG.info("[Client] start call channel id: {}", channel.id().asLongText());

        // 对于信息的写入，实际上有着严格的要求。
        // writeAndFlush 实际是一个异步的操作，直接使用 sync() 可以看到异常信息。
        // 支持的必须是 ByteBuf
        channel.writeAndFlush(rpcRequest).syncUninterruptibly();
        LOG.info("[Client] start call remote with request: {}", rpcRequest);
        final InvokeManager invokeManager = proxyContext.invokeService();
        invokeManager.addRequest(seqId, proxyContext.timeout());

        // 获取结果
        CallTypeStrategy callTypeStrategy = CallTypeStrategyFactory.callTypeStrategy(proxyContext.callType());
        RpcResponse rpcResponse = callTypeStrategy.result(proxyContext, rpcRequest);
        invokeManager.removeReqAndResp(seqId);

        // 获取调用结果
        context.rpcResponse(rpcResponse);
        FailStrategy failStrategy = FailStrategyFactory.failStrategy(proxyContext.failType());
        return failStrategy.fail(context);
    }

    /**
     * 执行过滤
     * （1）后期可以添加更多的路由等处理信息。
     * @param context 上下文
     * @since 0.0.9
     */
    private void doFilter(final RemoteInvokeContext context) {
        RpcFilter rpcFilter = new RandomBalanceFilter();
        rpcFilter.filter(context);
    }

}
