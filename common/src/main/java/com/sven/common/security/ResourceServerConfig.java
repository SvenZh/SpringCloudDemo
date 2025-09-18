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
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
public class ResourceServerConfig {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, PermitAllUrlConfig permitAllUrl,
            ObjectMapper objectMapper) throws Exception {
        httpSecurity
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .antMatchers("/login", "/oauth2/**", "/home").permitAll()
                        // 白名单
                        .antMatchers(ArrayUtils.toStringArray(permitAllUrl.getUrls().toArray())).permitAll()
                        // 其他所有的请求都需要鉴权
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                        // TOKEN解析器，提取请求中的TOKEN
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
                .oauth2Login(oauth2 ->{
                    oauth2
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/error")
                    ;
                })
                .exceptionHandling(exception -> exception.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/error")));
                ;

        return httpSecurity.build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(myClientRegistration());
    }

    private ClientRegistration myClientRegistration() {
        return ClientRegistration.withRegistrationId("myResourceServer")
            .clientId("openIdClient")
            .clientSecret("openIdClient")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("http://localhost:10050/login/oauth2/code/myResourceServer")
            .scope("openid", "image", "name", "phone")
            .authorizationUri("http://127.0.0.1:10030/oauth2/authorize")
            .tokenUri("http://127.0.0.1:10030/oauth2/token")
            .userInfoUri("http://127.0.0.1:10030/userinfo")
            .userNameAttributeName(IdTokenClaimNames.SUB)
            .jwkSetUri("http://127.0.0.1:10030/oauth2/jwks")
            .clientName("Sven")
            .build();
    }

}
