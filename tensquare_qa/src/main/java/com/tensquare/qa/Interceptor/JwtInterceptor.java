package com.tensquare.qa.Interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从请求头中获取授权信息Authorization
        String authorization = request.getHeader("Authorization");
        System.out.println("head====="+authorization);
        //处理授权信息
        if(!StringUtils.isEmpty(authorization)&&authorization.startsWith("Bearer ")){
            //取出token
            String token = authorization.substring(7);
            Claims claims = jwtUtil.parseJWT(token);
            if(claims!=null){
                //如果授权信息中有用户角色信息，就将角色信息放在request域中
                if("admin".equals(claims.get("roles"))){
                    request.setAttribute("admin",claims);
                }
                if("user".equals(claims.get("roles"))){
                    request.setAttribute("user",claims);
                }
            }
        }
        return true;
    }
}
