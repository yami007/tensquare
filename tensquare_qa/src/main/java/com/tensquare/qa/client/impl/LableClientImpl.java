package com.tensquare.qa.client.impl;

import com.tensquare.qa.client.LabelClient;
import entity.Result;
import entity.StatusCode;
import org.springframework.stereotype.Component;

@Component
public class LableClientImpl implements LabelClient{
    @Override
    public Result getById(String labelId) {
        System.out.println("进入到熔断器了");
        return new Result(false, StatusCode.ERROR,"熔断器启动，系统异常");
    }
}
