package com.sven.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Logger;
import feign.Logger.Level;

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
    public Level logger() {
        return Logger.Level.FULL;
    }
}
