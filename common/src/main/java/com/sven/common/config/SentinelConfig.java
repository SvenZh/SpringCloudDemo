package com.sven.common.config;

import org.apache.dubbo.spring.security.jackson.ObjectMapperCodecCustomer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;

@Configuration
public class SentinelConfig {

    /**
     * HTTP限流异常处理
     */
    @Bean
    public BlockExceptionHandler sentinelBlockExceptionHandler() {
        return new SentinelBlockExceptionHandler();
    }
}
