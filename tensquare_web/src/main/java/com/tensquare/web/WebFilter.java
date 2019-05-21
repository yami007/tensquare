package com.tensquare.web;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * filterType：返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过 滤器类型，具体如下：
 *pre ：可以在请求被路由之前调用
 *route ：在路由请求时候被调用
 *post ：在route和error过滤器之后
 */
@Component
public class WebFilter extends ZuulFilter{
    @Override
    public String filterType() {
        return "pre";  //前置过滤器
    }

    @Override
    public int filterOrder() {
        return 0; // 优先级为0，数字越大，优先级越低
    }

    @Override
    public boolean shouldFilter() {
        return true; // 是否执行该过滤器，此处为true，说明需要过滤
    }

    @Override
    public Object run() throws ZuulException {
        System.out.println("web网关过滤器启动");
        //向header中添加鉴权令牌
        RequestContext requestContext = RequestContext.getCurrentContext();
        //获取header
        HttpServletRequest request = requestContext.getRequest();
        String authorization = request.getHeader("Authorization");
        //通过zull网关向请求头中设置值
        if(!StringUtils.isEmpty(authorization)){
            requestContext.addZuulRequestHeader("Authorization",authorization);
        }
        return null;
    }
}
