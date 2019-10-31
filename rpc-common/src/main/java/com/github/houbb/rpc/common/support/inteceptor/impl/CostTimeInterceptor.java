package com.github.houbb.rpc.common.support.inteceptor.impl;

import com.github.houbb.heaven.util.time.impl.Times;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.common.support.inteceptor.InterceptorContext;

/**
 * 内置耗时 rpc 拦截器实现
 * @author binbin.hou
 * @since 0.1.4
 */
public class CostTimeInterceptor extends InterceptorAdaptor {

    private static final Log LOG = LogFactory.getLog(CostTimeInterceptor.class);

    @Override
    public void before(InterceptorContext context) {
        context.startTime(Times.systemTime());
    }

    @Override
    public void after(InterceptorContext context) {
        long costMills = Times.systemTime() - context.startTime();
        LOG.info("[Interceptor] cost time {} mills for traceId: {}", costMills,
                context.traceId());
    }

}
