package com.tensquare.base.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import entity.Result;
import entity.StatusCode;

@RestControllerAdvice
public class BaseExceptionHander {

    @ExceptionHandler(value = Exception.class)
    public Result errorHander(Exception e) {
        return new Result(false, StatusCode.ERROR, e.getMessage());

    }
}
