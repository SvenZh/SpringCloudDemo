package com.sven.auth;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.sven.common.app.ApplicationBuilder;

@SpringBootApplication
@EnableDiscoveryClient
public class AuthApplication {
    public static void main(String[] args) {
        ApplicationBuilder.run(AuthApplication.class, args);
    }
}
