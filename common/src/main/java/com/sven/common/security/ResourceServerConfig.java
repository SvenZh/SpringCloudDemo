package com.sven.common.security;


import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
public class ResourceServerConfig {

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private PermitAllUrlConfig permitAllUrl;
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
        .authorizeHttpRequests(authorizeRequests -> 
            authorizeRequests
                 // 白名单
                .antMatchers(ArrayUtils.toStringArray(permitAllUrl.getUrls().toArray())).permitAll()
                 // 其他所有的请求都需要鉴权
                .anyRequest().authenticated())
        .oauth2ResourceServer(oauth2ResourceServer -> 
            oauth2ResourceServer
            // 错误处理
            .authenticationEntryPoint(new CustomResourceAuthExceptionEntryPoint(objectMapper))
            // 获取TOKEN
            .bearerTokenResolver(new CustomBearerTokenExtractor(permitAllUrl))
            // OpaqueToken自省
            .opaqueToken().introspector(new CustomOpaqueTokenIntrospector("http://127.0.0.1:10030/oauth2/introspect", "admin", "admin"))
            )
        .headers()
        .frameOptions()
        .disable()
        .and()
        .csrf()
        .disable()
        ;

        return httpSecurity.build();
    }
}
