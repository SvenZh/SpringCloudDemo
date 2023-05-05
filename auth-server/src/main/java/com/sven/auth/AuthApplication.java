package com.sven.auth;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.sven.common.launch.ApplicationBuilder;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = { "com.sven.common.feign.client" })
public class AuthApplication {
    public static void main(String[] args) {
        ApplicationBuilder.run(AuthApplication.class, args);
    }
}
