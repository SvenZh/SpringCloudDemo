package com.sven.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

import com.sven.common.config.FeignConfig;
import com.sven.common.config.MyMetaObjectHandler;
import com.sven.common.config.MybatisPlusConfig;
import com.sven.common.config.ValidatorConfiguration;
import com.sven.common.exception.GlobalExceptionHandler;
import com.sven.common.security.EnableResourceServer;

@EnableResourceServer
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = { "com.sven.common.feign.client" }, defaultConfiguration = FeignConfig.class)
@Import(value = {GlobalExceptionHandler.class, ValidatorConfiguration.class, MybatisPlusConfig.class, MyMetaObjectHandler.class })
public class SystemServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemServiceApplication.class, args);
    }
}
