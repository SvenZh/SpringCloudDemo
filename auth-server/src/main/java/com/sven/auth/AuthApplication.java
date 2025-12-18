package com.sven.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import com.sven.common.config.FeignConfig;
import com.sven.common.config.RedisTemplateConfig;
import com.sven.common.config.SentinelConfig;
import com.sven.common.exception.GlobalExceptionHandler;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = { "com.sven.common.feign.client" }, defaultConfiguration = FeignConfig.class)
@Import(value = {GlobalExceptionHandler.class, RedisTemplateConfig.class, SentinelConfig.class })
@ImportResource(value = {"classpath:conf/dubbo-context.xml"})
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
