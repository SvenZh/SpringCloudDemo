package com.sven.gateway.porp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import lombok.Data;

@Data
@RefreshScope
@ConfigurationProperties("app.oauth.whitelist")
public class AuthProperties {
    private final List<String> skipUrl = new ArrayList<>();
}
