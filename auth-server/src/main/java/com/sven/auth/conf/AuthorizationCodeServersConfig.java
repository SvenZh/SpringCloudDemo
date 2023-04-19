package com.sven.auth.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;

@Configuration
public class AuthorizationCodeServersConfig {

    @Bean
    public AuthorizationCodeServices authorizationCodeServers() {
        return new InMemoryAuthorizationCodeServices();
    }
}
