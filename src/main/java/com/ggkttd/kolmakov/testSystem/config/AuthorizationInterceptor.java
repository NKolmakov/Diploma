package com.ggkttd.kolmakov.testSystem.config;

import com.ggkttd.kolmakov.testSystem.domain.User;
import com.ggkttd.kolmakov.testSystem.utils.UserRoles;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        String url = request.getRequestURL().toString();
        if (user != null && user.isAuthorized()) {
            if (user.getRole().getName() == UserRoles.STUDENT && url.matches(".*student/.*"))
                return true;
            if (user.getRole().getName() == UserRoles.TUTOR && url.matches(".*tutor/.*"))
                return true;
            if (user.getRole().getName() == UserRoles.ADMINISTRATOR && url.matches(".*administrator/.*"))
                return true;
        }
        response.sendRedirect("/authorization");
        return false;
    }
}
