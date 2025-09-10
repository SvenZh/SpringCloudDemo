package com.sven.auth.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.sven.auth.provider.CustomDaoAuthenticationProvider;

@EnableWebSecurity
public class WebSecurityConfiguration {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // 白名单
                        .antMatchers("/test/captcha", "/test/reg").permitAll()
                        // 其它请求都需要认证
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())
                .formLogin(Customizer.withDefaults())
                .headers(headers -> headers.frameOptions(frameOptionsCustomizer -> frameOptionsCustomizer.sameOrigin()));

        httpSecurity.authenticationProvider(new CustomDaoAuthenticationProvider());
        return httpSecurity.build();
    }
    
    @Bean
    @Order(0)
    SecurityFilterChain resources(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .requestMatchers((matchers) -> matchers.antMatchers("/actuator/**", "/css/**", "/error"))
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll())
                .requestCache(cache -> cache.disable())
                .securityContext(context -> context.disable())
                .sessionManagement(management -> management.disable());
        
        return httpSecurity.build();
    }
}
