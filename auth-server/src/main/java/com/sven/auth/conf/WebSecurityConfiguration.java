package com.sven.auth.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.sven.auth.provider.CustomDaoAuthenticationProvider;

@EnableWebSecurity
public class WebSecurityConfiguration {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(authorizeRequests -> authorizeRequests
//            .antMatchers("/test/**").permitAll()      // 白名单
            .anyRequest().authenticated())
            .formLogin().and()
            .headers()
            .frameOptions()
            .sameOrigin()                               // 避免iframe同源无法登录
            ;
        
        httpSecurity.authenticationProvider(new CustomDaoAuthenticationProvider());
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
