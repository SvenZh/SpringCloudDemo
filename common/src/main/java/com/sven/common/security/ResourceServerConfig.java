package com.sven.common.security;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.fasterxml.jackson.databind.ObjectMapper;

@RefreshScope
@Configuration
@EnableWebSecurity
public class ResourceServerConfig {

    @Value("${security.oauth2.ignore}")
    private List<String> ignore;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
        .authorizeHttpRequests(authorizeRequests -> 
            authorizeRequests
                .antMatchers(ArrayUtils.toStringArray(ignore.toArray())).permitAll()
                .anyRequest().authenticated())
        .oauth2ResourceServer(oauth2ResourceServer -> 
            oauth2ResourceServer
            // 错误处理
            .authenticationEntryPoint(new CustomResourceAuthExceptionEntryPoint(objectMapper))
            // 获取TOKEN
            .bearerTokenResolver(new CustomBearerTokenExtractor())
            .opaqueToken()
            )
        .headers()
        .frameOptions()
        .disable()
        .and()
        .csrf()
        .disable();
        ;

        return httpSecurity.build();
    }
}
