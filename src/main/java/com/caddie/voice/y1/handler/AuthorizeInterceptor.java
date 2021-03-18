package com.caddie.voice.y1.handler;

import com.caddie.voice.y1.exception.common.ExceptionType;
import com.caddie.voice.y1.exception.exceptions.UnauthorizedException;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthorizeInterceptor extends HandlerInterceptorAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthorizeInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("context path : " + request.getRequestURI());
        System.out.println("context path : " + request.getPathInfo());
        System.out.println("context path : " + request.getServletPath());
        System.out.println("request : " + request.toString());

        final String token = request.getHeader("Authorization");
        if (StringUtils.equals(request.getMethod(), "OPTIONS")) {
            return true;
        }

        if(!(request.getServletPath().startsWith("/vc-settlement/v1/login") || request.getServletPath().startsWith("/vc-settlement/v1/refresh-token"))) {
            if (token == null || token.isEmpty()) {
                throw new UnauthorizedException(ExceptionType.UnauthorizedException);
            }

            boolean isValidation = jwtTokenProvider.validateToken(token);
            if (!isValidation) {
                throw new UnauthorizedException(ExceptionType.AuthExpireUserException);
            }
        }

        return super.preHandle(request, response, handler);
    }
}
