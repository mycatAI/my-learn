package com.itheima.filter;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.logging.LogRecord;
//@WebFilter(urlPatterns = "/*")
public class DemoFilter implements Filter {
    @Override//初始化方法 ，只调用一次
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("init : 初始化方法进行了,放心前逻辑");
        //放行
        filterChain.doFilter(servletRequest ,servletResponse);
        System.out.println("init : 初始化方法进行了,放行后逻辑");
    }

    @Override//拦截请求之后调用，调用多次
    public void init(FilterConfig filterConfig) throws ServletException {
      System.out.println("拦截到了请求");
    }

    @Override//摧毁方法 ，只调用一次
    public void destroy() {
        System.out.println("destroy : 摧毁方法执行了");
    }
}
