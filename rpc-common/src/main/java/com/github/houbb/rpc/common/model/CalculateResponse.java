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
public class CalculateResponse implements Serializable {

    private static final long serialVersionUID = -1972014736222511341L;

    /**
     * 是否成功
     */
   private boolean success;

    /**
     * 二者的和
     */
   private int sum;

    public CalculateResponse() {
    }

    public CalculateResponse(boolean success, int sum) {
        this.success = success;
        this.sum = sum;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "CalculateResponse{" +
                "success=" + success +
                ", sum=" + sum +
                '}';
    }
}
