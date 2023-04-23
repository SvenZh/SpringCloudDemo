package com.sven.system;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.sven.common.constant.ApplicationBuilder;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class SystemServiceApplication {
    public static void main(String[] args) {
        ApplicationBuilder.run(SystemServiceApplication.class, args);
    }
}
