package com.sven.business.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sven.business.interceptor.DemoInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // 添加自定义拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new DemoInterceptor());
    }

    // 添加统一前缀
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
    }
}
