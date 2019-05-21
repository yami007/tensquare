package tensquare.manager;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * filterType：返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过 滤器类型，具体如下：
 *pre ：可以在请求被路由之前调用
 *route ：在路由请求时候被调用
 *post ：在route和error过滤器之后
 */
@Component
public class WebFilter extends ZuulFilter{
    @Autowired
    private JwtUtil jwtUtil;

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
        //处理授权信息
        if(!StringUtils.isEmpty(authorization)&&authorization.startsWith("Bearer ")){
            //取出token
            String token = authorization.substring(7);
            Claims claims = jwtUtil.parseJWT(token);
            if(claims!=null){
                //如果授权信息中有用户角色信息，就将角色信息放在request域中
                if("admin".equals(claims.get("roles"))){
                    request.setAttribute("admin",claims);
                    requestContext.addZuulRequestHeader("Authorization",authorization);
                    System.out.println("Token验证通过，是管理员角色"+authorization);
                    return null;
                }
            }
        }
        requestContext.setSendZuulResponse(false);//终止运行
        requestContext.setResponseStatusCode(401);//http状态码  
        requestContext.setResponseBody("无权访问");
        requestContext.getResponse().setContentType("text/html;charset=UTF-8");

        return null;
    }
}
