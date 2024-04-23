package com.sven.batch;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.sven.common.config.FeignConfig;
import com.sven.common.launch.ApplicationBuilder;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = { "com.sven.common.feign.client" }, defaultConfiguration = FeignConfig.class)
public class BatchApplication {
    public static void main(String[] args) {
        ApplicationBuilder.run(BatchApplication.class, args);
    }
}
