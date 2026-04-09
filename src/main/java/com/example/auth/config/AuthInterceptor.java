package com.example.auth.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
        HttpServletRequest request
        , HttpServletResponse response
        , Object handler
    ) throws Exception {

        HttpSession session = request.getSession(false);

        boolean loggedIn = (session != null && session.getAttribute("userId") != null);

        String uri = request.getRequestURI();

        // allow public pages
        if (uri.startsWith("/login") || uri.startsWith("/css") || uri.startsWith("/js") || uri.startsWith("/images")) {
            return true;
        }

        if (!loggedIn) {
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}