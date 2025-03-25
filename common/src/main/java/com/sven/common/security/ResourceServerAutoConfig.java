package com.sven.common.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

import feign.RequestInterceptor;

@Configuration
@EnableConfigurationProperties(value = {PermitAllUrlConfig.class})
public class ResourceServerAutoConfig {
    
    @Bean
    public BearerTokenResolver bearerTokenExtractor(PermitAllUrlConfig permitAllUrlConfig) {
        return new CustomBearerTokenExtractor(permitAllUrlConfig);
    }
    
    @Bean
    public RequestInterceptor oauthRequestInterceptor(BearerTokenResolver tokenResolver) {
        return new CustomOAuth2AccessTokenInterceptor(tokenResolver);
    }
    
    @Bean("pms")
    public PermissionService permissionService() {
        return new PermissionService();
    }
}
