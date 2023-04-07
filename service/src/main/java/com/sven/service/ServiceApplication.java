package com.sven.service;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.sven.common.app.ApplicationBuilder;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceApplication {
    public static void main(String[] args) {
        ApplicationBuilder.run(ServiceApplication.class, args);
    }
}
