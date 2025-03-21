package com.sven.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

import com.sven.common.security.CustomBearerTokenExtractor;
import com.sven.common.security.CustomOAuth2AccessTokenInterceptor;

import feign.Logger;
import feign.Logger.Level;
import feign.RequestInterceptor;

@Configuration
public class FeignConfig {
    
//    @Bean
//    public SetterFactory setterFactory() {
//        return (target, method) -> {
//            String groupKey = target.name();
//            String commandKey = target.name();
//            
//            return HystrixCommand.Setter
//                    .withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey))
//                    .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey));
//        };
//    }
    
    @Bean
    public BearerTokenResolver bearerTokenExtractor() {
        return new CustomBearerTokenExtractor();
    }
    
    @Bean
    public RequestInterceptor oauthRequestInterceptor(BearerTokenResolver tokenResolver) {
        return new CustomOAuth2AccessTokenInterceptor(tokenResolver);
    }
    
    @Bean
    public Level logger() {
        return Logger.Level.FULL;
    }
}
