package com.sven.business;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

import com.sven.common.config.MybatisPlusConfig;
import com.sven.common.config.Swagger3Config;
import com.sven.common.config.ValidatorConfiguration;
import com.sven.common.launch.ApplicationBuilder;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = { "com.sven.common.feign.client" })
@Import(value = { Swagger3Config.class, ValidatorConfiguration.class, MybatisPlusConfig.class })
public class BusinessServiceApplication {
    public static void main(String[] args) {
        ApplicationBuilder.run(BusinessServiceApplication.class, args);
    }
}
