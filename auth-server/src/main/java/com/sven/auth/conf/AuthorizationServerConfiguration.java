package com.sven.auth.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationConsentAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.sven.auth.convert.CustomOAuth2PasswordAuthorizationConvert;
import com.sven.auth.convert.CustomOAuth2SmsAuthorizationConvert;
import com.sven.auth.filter.ValidateCodeFilter;
import com.sven.auth.provider.CustomDaoAuthenticationProvider;
import com.sven.auth.provider.CustomOAuth2PasswordAuthorizationProvider;
import com.sven.auth.provider.CustomOAuth2SmsAuthorizationProvider;
import com.sven.auth.service.RedisOAuth2AuthorizationService;
import com.sven.auth.token.CustomeOAuth2TokenCustomizer;
import com.sven.auth.token.generator.CustomOAuth2AuthorizationCodeGenerator;
import com.sven.auth.token.generator.CustomeOAuth2AccessTokenGenerator;

@Configuration
public class AuthorizationServerConfiguration {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Bean
    public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        
        httpSecurity.addFilterBefore(new ValidateCodeFilter(redisTemplate), UsernamePasswordAuthenticationFilter.class);
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
        // oauth2.0的配置托管给 SpringSecurity
        httpSecurity.apply(authorizationServerConfigurer);
        
        authorizationServerConfigurer
            .authorizationEndpoint(authorizationEndpoint -> {
                authorizationEndpoint.authenticationProviders(authenticationProviders -> {
                    authenticationProviders.forEach(authenticationProvider -> {
                        if (authenticationProvider instanceof OAuth2AuthorizationCodeRequestAuthenticationProvider) {
                            // 无授权同意页面的授权码生成方式
                            ((OAuth2AuthorizationCodeRequestAuthenticationProvider) authenticationProvider)
                                    .setAuthorizationCodeGenerator(new CustomOAuth2AuthorizationCodeGenerator());
                        }

                        if (authenticationProvider instanceof OAuth2AuthorizationConsentAuthenticationProvider) {
                            // 授权同意页面的授权码生成方式
                            ((OAuth2AuthorizationConsentAuthenticationProvider) authenticationProvider)
                                    .setAuthorizationCodeGenerator(new CustomOAuth2AuthorizationCodeGenerator());
                        }
                    });
                });
                
            })
            .tokenEndpoint(tokenEndpoint -> {
                // 自定义授权模式Token转换器
                tokenEndpoint.accessTokenRequestConverter(new CustomOAuth2PasswordAuthorizationConvert());
                tokenEndpoint.accessTokenRequestConverter(new CustomOAuth2SmsAuthorizationConvert());
            })
            ;
        
        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        SecurityFilterChain securityFilterChain = httpSecurity
            .requestMatcher(endpointsMatcher)       // 拦截授权服务器的相关请求
            .authorizeHttpRequests(authorizeRequests -> {
                    authorizeRequests.anyRequest().authenticated();
             })
            .csrf(csrf -> csrf.disable())
            .formLogin(Customizer.withDefaults())
            .sessionManagement(sm -> sm.disable())
            .headers(headers -> headers.cacheControl(cacheControl -> cacheControl.disable()))
            .build();

        // 自定义授权模式验证器
        addCustomOAuth2AuthenticationProvider(httpSecurity);
        return securityFilterChain;
    }
    
    @SuppressWarnings("unchecked")
    private void addCustomOAuth2AuthenticationProvider(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManager authenticationManager = httpSecurity.getSharedObject(AuthenticationManager.class);
        OAuth2AuthorizationService authorizationService = httpSecurity.getSharedObject(OAuth2AuthorizationService.class);
        
        CustomOAuth2PasswordAuthorizationProvider customOAuth2PasswordAuthorizationProvider = new CustomOAuth2PasswordAuthorizationProvider(
                authenticationManager, authorizationService, oAuth2TokenGenerator());
        CustomOAuth2SmsAuthorizationProvider customOAuth2SmsAuthorizationProvider = new CustomOAuth2SmsAuthorizationProvider(
                authenticationManager, authorizationService, oAuth2TokenGenerator());

        httpSecurity.authenticationProvider(new CustomDaoAuthenticationProvider());
        httpSecurity.authenticationProvider(customOAuth2PasswordAuthorizationProvider);
        httpSecurity.authenticationProvider(customOAuth2SmsAuthorizationProvider);
        
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }
    
//    @Bean
//    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
//            RegisteredClientRepository registeredClientRepository) {
//        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
//    }
    
    @Bean
    public OAuth2AuthorizationService authorizationService(RedisTemplate<String, Object> redisTemplate) {
        return new RedisOAuth2AuthorizationService(redisTemplate);
    }
    
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
            RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }
    
    @Bean
    public OAuth2TokenGenerator<? extends OAuth2Token> oAuth2TokenGenerator() {
        CustomeOAuth2AccessTokenGenerator accessTokenGenerator = new CustomeOAuth2AccessTokenGenerator();
        accessTokenGenerator.setAccessTokenCustomizer(new CustomeOAuth2TokenCustomizer());
        return new DelegatingOAuth2TokenGenerator(accessTokenGenerator, new OAuth2RefreshTokenGenerator());
    }
    
    @Bean 
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }
}
