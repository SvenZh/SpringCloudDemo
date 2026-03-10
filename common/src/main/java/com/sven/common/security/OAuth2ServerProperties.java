package com.sven.common.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "security.oauth2.server")
@Getter
@Setter
public class OAuth2ServerProperties {
    private String issuerUri;
    private String introspectionUri;
    private String jwkSetUri;
    private String tokenUri;
    private String authorizationUri;
    private String userInfoUri;
}
