package com.sven.business.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class ResourceServerConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(authorizeRequests -> {
            authorizeRequests.anyRequest().authenticated();
        }).oauth2ResourceServer(oauth2ResourceServer -> {
            oauth2ResourceServer.jwt();
        })
        ;

        return httpSecurity.build();
    }
}
