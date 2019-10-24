/*
 * Copyright (c)  2019. houbinbin Inc.
 * rpc All rights reserved.
 */

package com.github.houbb.rpc.register.simple.server.impl;

import com.github.houbb.heaven.util.common.ArgUtil;
import com.github.houbb.heaven.util.guava.Guavas;
import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.register.domain.entry.ServerEntry;
import com.github.houbb.rpc.register.simple.server.ServerRegisterService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> 默认服务注册类 </p>
 *
 * <pre> Created: 2019/10/23 9:16 下午  </pre>
 * <pre> Project: rpc  </pre>
 *
 * @author houbinbin
 * @since 0.0.8
 */
public class DefaultServerRegisterService implements ServerRegisterService {

    private static final Log LOG = LogFactory.getLog(DefaultServerRegisterService.class);

    /**
     * 存放对应的 map 信息
     * @since 0.0.8
     */
    private final Map<String, Set<ServerEntry>> map;

    public DefaultServerRegisterService(){
        map = new ConcurrentHashMap<>();
    }

    @Override
    public List<ServerEntry> register(ServerEntry serverEntry) {
        paramCheck(serverEntry);

        final String serviceId = serverEntry.serviceId();
        Set<ServerEntry> serverEntrySet = map.get(serviceId);
        if(ObjectUtil.isNull(serverEntrySet)) {
            serverEntrySet = Guavas.newHashSet();
        }

        LOG.info("[Register Server] add service: {}", serverEntry);
        serverEntrySet.add(serverEntry);
        map.put(serviceId, serverEntrySet);

        // 返回更新后的结果
        return Guavas.newArrayList(serverEntrySet);
    }

    @Override
    public List<ServerEntry> unRegister(ServerEntry serverEntry) {
        paramCheck(serverEntry);

        final String serviceId = serverEntry.serviceId();
        Set<ServerEntry> serverEntrySet = map.get(serviceId);

        if(CollectionUtil.isEmpty(serverEntrySet)) {
            // 服务列表为空
            LOG.info("[Register Server] remove service set is empty. entry: {}", serverEntry);
            return Guavas.newArrayList();
        }

        serverEntrySet.remove(serverEntry);
        LOG.info("[Register Server] remove service: {}", serverEntry);
        map.put(serviceId, serverEntrySet);

        // 返回更新后的结果
        return Guavas.newArrayList(serverEntrySet);
    }

    @Override
    public List<ServerEntry> lookUp(String serviceId) {
        ArgUtil.notEmpty(serviceId, "serviceId");

        LOG.info("[Register Server] start lookUp serviceId: {}", serviceId);
        Set<ServerEntry> serverEntrySet = map.get(serviceId);
        return Guavas.newArrayList(serverEntrySet);
    }

    /**
     * 参数校验
     * @param serverEntry 服务明细
     * @since 0.0.8
     */
    private void paramCheck(final ServerEntry serverEntry) {
        ArgUtil.notNull(serverEntry, "serviceEntry");
        final String serviceId = serverEntry.serviceId();
        ArgUtil.notEmpty(serviceId, "serviceId");
    }

}
