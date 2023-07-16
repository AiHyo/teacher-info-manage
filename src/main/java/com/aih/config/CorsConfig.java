package com.aih.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter(){
        //1.添加CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
//        config.addAllowedOrigin("http://47.113.150.138:9998"); //这里填写请求的前端服务器
        //允许所有域 写*，cookie无法使用
        config.addAllowedOrigin("*");
        //2) 是否发送Cookie信息
        config.setAllowCredentials(true);
        //3) 允许的请求方式
        config.addAllowedMethod("*");
        // 4）允许的头信息 (跨域访问默认不能获取全部头部信息）
        config.addAllowedHeader("*");

        //2.添加映射路径，我们拦截一切请求
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);

        //3.返回新的CorsFilter.
        return new CorsFilter(configSource);
    }
}
