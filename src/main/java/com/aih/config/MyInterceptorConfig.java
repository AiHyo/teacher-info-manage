package com.aih.config;


import com.aih.common.interceptor.JwtValidateInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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
                        "/teacher/logout",
                        "/error",
                        "/swagger-ui/**",
                        "/swagger-resources/**"
                );
//        interceptor.excludePathPatterns("http://47.113.150.138:9998/");
    }

    //静态资源过滤
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/static/**").addResourceLocations("file:D:/static/");
    }
}
