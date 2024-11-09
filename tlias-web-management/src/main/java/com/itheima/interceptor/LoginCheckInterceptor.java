package com.itheima.interceptor;

import com.itheima.pojo.Result;
import com.itheima.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
@Component//ioc容器
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override//目标资源方法运行前，返回ture：放行 ，返回false：拦截
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("prehandle");
        //获取url
        String url = request.getRequestURL().toString();
        //2.判断url是否包含login
        if(url.contains("login")){
            log.info("放行操作，登录");
           return true;
        }
        //3.获取请求头令牌
        String jwt = request.getHeader("token");
        //4.判断请求头令牌是否为空
        if(!StringUtils.hasLength(jwt)){
            log.info("请求头为空：返回未登录信息");
            Result error = Result.error("NOT_LOGIN");
            //手动封装
            JSONObject jsonObject = new JSONObject(error);
            String login = jsonObject.toString();
            response.getWriter().write(login);
            return false ;
        }
        // 5.解析jwt令牌
        try{
            JwtUtils.parseJWT(jwt);
        }catch(Exception e){
            e.printStackTrace();
            Result error = Result.error("NOT_LOGIN");
            //手动封装
            JSONObject jsonObject = new JSONObject(error);
            String login = jsonObject.toString();
            response.getWriter().write(login);
            return false ;
        }
        //6.放行
        log.info("放行：");
        return true;
    }

    @Override//目标资源方法运行后运行
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
       System.out.println("postHandle");
    }

    @Override//视图渲染完毕后执行，最后执行
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion");
    }
}
