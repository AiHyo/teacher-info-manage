package com.aih.config;


import com.aih.interceptor.JwtValidateInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyInterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private JwtValidateInterceptor jwtValidateInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptor = registry.addInterceptor(jwtValidateInterceptor);
        interceptor.addPathPatterns("/**")
                .excludePathPatterns(
                        "/teacher/login",
                        "/teacher/info",
                        "/teacher/logout",
                        "/error",
                        "/swagger-ui/**",
                        "/swagger-resources/**"
                );
    }
}
