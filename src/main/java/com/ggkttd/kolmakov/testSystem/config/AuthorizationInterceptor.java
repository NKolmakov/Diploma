package com.ggkttd.kolmakov.testSystem.config;

import com.ggkttd.kolmakov.testSystem.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User)request.getSession().getAttribute("user");
        if(user != null && user.isAuthorized()){
            return true;
        }
        response.sendRedirect("/authorization");
        return false;
    }
}
