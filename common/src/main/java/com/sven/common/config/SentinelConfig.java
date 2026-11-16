package com.sven.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.csp.sentinel.adapter.spring.webmvc_v6x.callback.BlockExceptionHandler;


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
