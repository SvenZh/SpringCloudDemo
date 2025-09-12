package com.sven.common.security;


import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
public class ResourceServerConfig {

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private PermitAllUrlConfig permitAllUrl;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // 白名单
                        .antMatchers(ArrayUtils.toStringArray(permitAllUrl.getUrls().toArray())).permitAll()
                        // 其他所有的请求都需要鉴权
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                        // 获取TOKEN
                        .bearerTokenResolver(new CustomBearerTokenExtractor(permitAllUrl))
                        // 认证失败处理
                        .authenticationEntryPoint(new CustomResourceAuthExceptionEntryPoint(objectMapper))
                        // 认证成功, 权限不足处理
                        .accessDeniedHandler(new CustomResourceAccessDeniedHandler(objectMapper))
                        // OpaqueToken自省
                        .opaqueToken().introspector(new CustomOpaqueTokenIntrospector("http://127.0.0.1:10030/oauth2/introspect", "admin", "admin", redisTemplate)))
                .headers(header -> header
                        .frameOptions().disable()
                        // 禁用浏览器或代理headers缓存
                        .cacheControl(HeadersConfigurer.CacheControlConfig::disable))
                .csrf(csrf -> csrf.disable())
                // .oauth2Login(Customizer.withDefaults())
                ;

        return httpSecurity.build();
    }
}
