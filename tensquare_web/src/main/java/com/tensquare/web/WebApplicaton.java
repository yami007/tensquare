package com.tensquare.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class WebApplicaton {

    public static void main(String[] args) {
        SpringApplication.run(WebApplicaton.class, args);
    }
}
