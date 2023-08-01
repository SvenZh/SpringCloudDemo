package com.sven.gateway;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.sven.common.launch.ApplicationBuilder;

@SpringCloudApplication
@EnableFeignClients(basePackages = { "com.sven.common.feign.client" })
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
public class GateWayApplication {
    public static void main(String[] args) {
        ApplicationBuilder.run(GateWayApplication.class, args);
    }
}
