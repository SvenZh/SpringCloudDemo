package com.sven.gateway;

import org.springframework.cloud.client.SpringCloudApplication;

import com.sven.common.launch.ApplicationBuilder;

@SpringCloudApplication
public class GateWayApplication {
    public static void main(String[] args) {
        ApplicationBuilder.run(GateWayApplication.class, args);
    }
}
