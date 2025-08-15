package com.sathwikhbhat.scm.config;

import com.sathwikhbhat.scm.entity.User;
import com.sathwikhbhat.scm.helpers.Helper;
import com.sathwikhbhat.scm.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.security.Principal;

@Configuration
public class UsersInterceptorConfig implements WebMvcConfigurer, HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/verify-email");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uri = request.getRequestURI();
        if (uri.startsWith("/user/verify-email")) {
            return true;
        }

        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            String email = Helper.getEmailOfLoggedInUser(principal);
            User user = userService.getUserByEmail(email);
            if (user == null || !user.isEnabled()) {
                response.sendRedirect("/user/verify-email");
                return false;
            }
        }

        return true;
    }
}