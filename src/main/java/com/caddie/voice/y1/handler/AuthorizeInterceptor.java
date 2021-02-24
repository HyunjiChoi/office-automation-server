package com.caddie.voice.y1.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthorizeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("context path : " + request.getRequestURI());
        System.out.println("context path : " + request.getPathInfo());
        System.out.println("context path : " + request.getServletPath());
        System.out.println("request : " + request.toString());
        return true;
    }
}
