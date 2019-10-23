/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.register.api;

import com.github.houbb.rpc.register.domain.ServerEntry;

import java.util.List;

/**
 * <p> 通知监听器 </p>
 *
 * <pre> Created: 2019/10/23 8:04 下午  </pre>
 * <pre> Project: rpc  </pre>
 *
 * @author houbinbin
 * @since 0.0.8
 */
@Deprecated
public interface NotifyListener {

    /**
     * 通知监听列表
     * @param serviceEntries 服务明细信息
     * @since 0.0.8
     */
    void listen(final List<ServerEntry> serviceEntries);

}
