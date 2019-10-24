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
import com.github.houbb.rpc.register.domain.entry.ServiceEntry;
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
    private final Map<String, Set<ServiceEntry>> map;

    public DefaultServerRegisterService(){
        map = new ConcurrentHashMap<>();
    }

    @Override
    public List<ServiceEntry> register(ServiceEntry serviceEntry) {
        paramCheck(serviceEntry);

        final String serviceId = serviceEntry.serviceId();
        Set<ServiceEntry> serviceEntrySet = map.get(serviceId);
        if(ObjectUtil.isNull(serviceEntrySet)) {
            serviceEntrySet = Guavas.newHashSet();
        }

        LOG.info("[Register Server] add service: {}", serviceEntry);
        serviceEntrySet.add(serviceEntry);
        map.put(serviceId, serviceEntrySet);

        // 返回更新后的结果
        return Guavas.newArrayList(serviceEntrySet);
    }

    @Override
    public List<ServiceEntry> unRegister(ServiceEntry serviceEntry) {
        paramCheck(serviceEntry);

        final String serviceId = serviceEntry.serviceId();
        Set<ServiceEntry> serviceEntrySet = map.get(serviceId);

        if(CollectionUtil.isEmpty(serviceEntrySet)) {
            // 服务列表为空
            LOG.info("[Register Server] remove service set is empty. entry: {}", serviceEntry);
            return Guavas.newArrayList();
        }

        serviceEntrySet.remove(serviceEntry);
        LOG.info("[Register Server] remove service: {}", serviceEntry);
        map.put(serviceId, serviceEntrySet);

        // 返回更新后的结果
        return Guavas.newArrayList(serviceEntrySet);
    }

    @Override
    public List<ServiceEntry> lookUp(String serviceId) {
        ArgUtil.notEmpty(serviceId, "serviceId");

        LOG.info("[Register Server] start lookUp serviceId: {}", serviceId);
        Set<ServiceEntry> serviceEntrySet = map.get(serviceId);
        return Guavas.newArrayList(serviceEntrySet);
    }

    /**
     * 参数校验
     * @param serviceEntry 服务明细
     * @since 0.0.8
     */
    private void paramCheck(final ServiceEntry serviceEntry) {
        ArgUtil.notNull(serviceEntry, "serviceEntry");
        final String serviceId = serviceEntry.serviceId();
        ArgUtil.notEmpty(serviceId, "serviceId");
    }

}
