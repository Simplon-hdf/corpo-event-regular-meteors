package org.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/events/**")
                .excludePathPatterns("/", "/login/**", "/h2-console/**", "/css/**", "/js/**");
    }

    private static class AuthInterceptor extends HandlerInterceptorAdapter {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            String requestURI = request.getRequestURI();
            HttpSession session = request.getSession(true);

            if (session.getAttribute("currentUser") == null && 
                !requestURI.equals("/") && 
                !requestURI.startsWith("/login")) {
                
                response.sendRedirect("/");
                return false;
            }
            return true;
        }
    }
} 