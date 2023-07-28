package com.sven.gateway;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;

import com.sven.common.launch.ApplicationBuilder;

@SpringCloudApplication
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
public class GateWayApplication {
    public static void main(String[] args) {
        ApplicationBuilder.run(GateWayApplication.class, args);
    }
}
