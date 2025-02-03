package org.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.lang.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.example.service.UserService;
import org.example.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private UserService userService;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/events/**", "/admin/**")
                .excludePathPatterns("/", "/login/**", "/h2-console/**", "/css/**", "/js/**");
    }

    private class AuthInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(@NonNull HttpServletRequest request, 
                               @NonNull HttpServletResponse response, 
                               @NonNull Object handler) throws Exception {
            String requestURI = request.getRequestURI();
            HttpSession session = request.getSession(true);

            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser != null) {
                currentUser = userService.findByEmail(currentUser.getEmail());
                session.setAttribute("currentUser", currentUser);
            }

            if (currentUser == null && 
                !requestURI.equals("/") && 
                !requestURI.startsWith("/login")) {
                
                response.sendRedirect("/");
                return false;
            }
            return true;
        }
    }
} 