package com.sven.business;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.sven.common.app.ApplicationBuilder;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class BusinessServiceApplication {
    public static void main(String[] args) {
        ApplicationBuilder.run(BusinessServiceApplication.class, args);
    }
}
