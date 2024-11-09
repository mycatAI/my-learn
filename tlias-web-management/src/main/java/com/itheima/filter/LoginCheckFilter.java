package com.itheima.filter;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.itheima.pojo.Result;
import com.itheima.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.json.JSONObject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
//@WebFilter(urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest rep = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1.获取请求url
         String url = rep.getRequestURL().toString();
         log.info("请求的url：{}" ,url);
        //2.判断是否是登录请求，
        if(url.contains("login")){
            log.info("登陆操作，放行");
            filterChain.doFilter(servletRequest,servletResponse);
            return ;
        }
       // 3.获取请求头token ，
         String jwt = rep.getHeader("token");
        //判断jwt是否存在
        if(!StringUtils.hasLength(jwt)){
            log.info("请求头为空，返回未登陆信息");
            Result error = Result.error("NOT_LOGIN");
            //手动封装 对象--json
            JSONObject jsonObject = new JSONObject(error);
            String login = jsonObject.toString();
            response.getWriter().write(login);
            return ;
        }
        //4.解析token
        try{
            JwtUtils.parseJWT(jwt);
        }catch(Exception e){
            e.printStackTrace();
            log.info("解析令失败 ， 返回登陆错误信息");
            Result error = Result.error("NOT_LOGIN");
            //手动封装 对象--json
            JSONObject jsonObject = new JSONObject(error);
            String login = jsonObject.toString();
            response.getWriter().write(login);
            return ;
        }
        // 5.放行
        log.info("linpaihef:");
        filterChain.doFilter(servletRequest,servletResponse);
       // 5.放行
    }
}
