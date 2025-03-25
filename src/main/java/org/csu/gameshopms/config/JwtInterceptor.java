package org.csu.gameshopms.config;

import org.csu.gameshopms.util.JwtUtil;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        if (uri.contains("/user/login") || uri.contains("/user/register")) {
            return true;
        }

        String token = request.getHeader("Authorization");
        System.out.println("JwtInterceptor - URI: " + uri + ", Authorization: " + token);
        if (token == null || !token.startsWith("Bearer ")) {
            System.out.println("JwtInterceptor - No valid token provided");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\": 401, \"message\": \"未授权，请登录\"}");
            return false;
        }

        token = token.substring(7);
        if (!JwtUtil.validateToken(token)) {
            System.out.println("JwtInterceptor - Invalid token: " + token);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\": 401, \"message\": \"token 无效，请重新登录\"}");
            return false;
        }

        System.out.println("JwtInterceptor - Token validated successfully");
        return true;
    }
}