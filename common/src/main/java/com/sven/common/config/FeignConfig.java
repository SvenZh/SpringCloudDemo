package com.sven.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;

import feign.hystrix.SetterFactory;

@Configuration
public class FeignConfig {
    
    @Bean
    public SetterFactory setterFactory() {
        return (target, method) -> {
            String groupKey = target.name();
            String commandKey = target.name();
            
            return HystrixCommand.Setter
                    .withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey))
                    .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey));
        };
    }
}
