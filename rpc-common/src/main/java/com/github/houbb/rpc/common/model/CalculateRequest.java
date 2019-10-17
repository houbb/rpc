/*
 * Copyright (c)  2018. houbinbin Inc.
 * fake All rights reserved.
 */

package com.github.houbb.rpc.common.model;

import java.io.Serializable;

/**
 * <p> 请求入参 </p>
 *
 * <pre> Created: 2018/8/24 下午5:05  </pre>
 * <pre> Project: fake  </pre>
 *
 * @author houbinbin
 * @since 0.0.3
 */
public class CalculateRequest implements Serializable {

    private static final long serialVersionUID = 6420751004355300996L;

    /**
     * 参数一
     */
    private int one;

    /**
     * 参数二
     */
    private int two;

    public CalculateRequest() {
    }

    public CalculateRequest(int one, int two) {
        this.one = one;
        this.two = two;
    }

    public int getOne() {
        return one;
    }

    public void setOne(int one) {
        this.one = one;
    }

    public int getTwo() {
        return two;
    }

    public void setTwo(int two) {
        this.two = two;
    }

    @Override
    public String toString() {
        return "CalculateRequest{" +
                "one=" + one +
                ", two=" + two +
                '}';
    }

}
