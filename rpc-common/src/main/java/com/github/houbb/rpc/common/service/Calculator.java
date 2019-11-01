/*
 * Copyright (c)  2018. houbinbin Inc.
 * fake All rights reserved.
 */

package com.github.houbb.rpc.common.service;

import com.github.houbb.rpc.common.model.CalculateRequest;
import com.github.houbb.rpc.common.model.CalculateResponse;

/**
 * <p> 计算服务接口 </p>
 *
 * <pre> Created: 2018/8/24 下午4:47  </pre>
 * <pre> Project: fake  </pre>
 *
 * @author houbinbin
 * @since 0.0.1
 */
public interface Calculator {

    /**
     * 计算加法
     * @param request 请求入参
     * @return 返回结果
     */
    CalculateResponse sum(final CalculateRequest request);

}
