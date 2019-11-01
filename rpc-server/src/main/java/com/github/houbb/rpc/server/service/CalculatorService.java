package com.github.houbb.rpc.server.service;

import com.github.houbb.rpc.common.model.CalculateRequest;
import com.github.houbb.rpc.common.model.CalculateResponse;
import com.github.houbb.rpc.common.service.Calculator;

/**
 * @author binbin.hou
 * @since 1.0.0
 */
public class CalculatorService implements Calculator {

    @Override
    public CalculateResponse sum(CalculateRequest request) {
        int sum = request.getOne()+request.getTwo();

        return new CalculateResponse(true, sum);
    }

}
