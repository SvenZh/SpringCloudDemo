package com.sven.auth.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class WebSecurityConfiguration {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(authorizeRequests -> authorizeRequests
            .anyRequest()
            .authenticated())
            .headers()
            .frameOptions()
            .sameOrigin()// 避免iframe同源无法登录
            ;
        return httpSecurity.build();
    }
    
    @Bean
    @Order(0)
    SecurityFilterChain resources(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.requestMatchers((matchers) -> matchers.antMatchers("/actuator/**", "/css/**", "/error"))
            .authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll())
            .requestCache()
            .disable()
            .securityContext()
            .disable()
            .sessionManagement()
            .disable();
        
        return httpSecurity.build();
    }
}
