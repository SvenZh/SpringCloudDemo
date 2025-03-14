package com.sven.business.conf;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class ResourceServerConfig {

    @Value("${security.oauth2.ignore}")
    private List<String> ignore;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(authorizeRequests -> {
            authorizeRequests
                    .antMatchers(ArrayUtils.toStringArray(ignore.toArray())).permitAll()
                    .anyRequest().authenticated();
        })
        
        .oauth2ResourceServer().jwt()
        ;

        return httpSecurity.build();
    }
}
