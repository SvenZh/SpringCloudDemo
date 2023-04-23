package com.sven.scheduler;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.sven.common.constant.ApplicationBuilder;

@SpringBootApplication
@EnableDiscoveryClient
public class SchedulerApplication {
    public static void main(String[] args) {
        ApplicationBuilder.run(SchedulerApplication.class, args);
    }
}
