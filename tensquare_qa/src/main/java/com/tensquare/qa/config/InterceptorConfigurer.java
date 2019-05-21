package com.tensquare.qa.config;

import com.tensquare.qa.Interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class InterceptorConfigurer extends WebMvcConfigurationSupport{
    //注入需要配置的拦截器
    @Autowired
    private JwtInterceptor jwtInterceptor;
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")   //需要拦截的路径
                .excludePathPatterns("/**/login");  //不需要拦截的路径
    }
}
