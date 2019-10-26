/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.common.rpc.invoke;

/**
 * <p> 调用上下文 </p>
 *
 * <pre> Created: 2019/10/26 9:30 上午  </pre>
 * <pre> Project: rpc  </pre>
 *
 * 核心目的：
 * （1）用于定义 filter 相关信息
 * （2）用于 load-balance 相关信息处理
 * @author houbinbin
 * @since 0.0.9
 */
public interface InvokeHandler {

    /**
     * 调用
     * @param invokeContext 调用上下文
     * @since 0.0.9
     */
    void invoke(final InvokeContext invokeContext);

}
