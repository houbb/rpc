package com.github.houbb.rpc.client.invoke.impl;

import com.github.houbb.heaven.util.guava.Guavas;
import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.client.core.RpcClient;
import com.github.houbb.rpc.client.invoke.InvokeService;
import com.github.houbb.rpc.common.exception.RpcRuntimeException;
import com.github.houbb.rpc.common.rpc.domain.RpcResponse;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 调用服务接口
 * @author binbin.hou
 * @since 0.0.6
 */
public class DefaultInvokeService implements InvokeService {

    private static final Log LOG = LogFactory.getLog(DefaultInvokeService.class);

    /**
     * 请求序列号集合
     * （1）这里后期如果要添加超时检测，可以添加对应的超时时间。
     * 可以把这里调整为 map
     * @since 0.0.6
     */
    private final Set<String> requestSet;

    /**
     * 响应结果
     * @since 0.0.6
     */
    private final ConcurrentHashMap<String, RpcResponse> responseMap;

    public DefaultInvokeService() {
        requestSet = Guavas.newHashSet();
        responseMap = new ConcurrentHashMap<>();
    }

    @Override
    public InvokeService addRequest(String seqId) {
        LOG.info("[Client] start add request for seqId: {}", seqId);
        requestSet.add(seqId);
        return this;
    }

    @Override
    public InvokeService addResponse(String seqId, RpcResponse rpcResponse) {
        // 这里放入之前，可以添加判断。
        // 如果 seqId 必须处理请求集合中，才允许放入。或者直接忽略丢弃。
        LOG.info("[Client] 获取结果信息，seq: {}, rpcResponse: {}", seqId, rpcResponse);
        responseMap.putIfAbsent(seqId, rpcResponse);

        // 通知所有等待方
        LOG.info("[Client] seq 信息已经放入，通知所有等待方", seqId);

        synchronized (this) {
            this.notifyAll();
        }

        return this;
    }

    @Override
    public RpcResponse getResponse(String seqId) {
        try {
            RpcResponse rpcResponse = this.responseMap.get(seqId);
            if(ObjectUtil.isNotNull(rpcResponse)) {
                LOG.info("[Client] seq {} 对应结果已经获取: {}", seqId, rpcResponse);
                return rpcResponse;
            }

            // 进入等待
            while (rpcResponse == null) {
                LOG.info("[Client] seq {} 对应结果为空，进入等待", seqId);
                // 同步等待锁
                synchronized (this) {
                    this.wait();
                }

                rpcResponse = this.responseMap.get(seqId);
                LOG.info("[Client] seq {} 对应结果已经获取: {}", seqId, rpcResponse);
            }

            return rpcResponse;
        } catch (InterruptedException e) {
            throw new RpcRuntimeException(e);
        }
    }
}
