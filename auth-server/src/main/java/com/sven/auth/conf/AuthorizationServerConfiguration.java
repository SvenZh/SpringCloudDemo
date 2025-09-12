package com.sven.auth.conf;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationConsentAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.sven.auth.convert.CustomOAuth2PasswordAuthorizationConvert;
import com.sven.auth.convert.CustomOAuth2SmsAuthorizationConvert;
import com.sven.auth.convert.CustomOidcUserInfoRequestConverter;
import com.sven.auth.filter.ValidateCodeFilter;
import com.sven.auth.handler.CustomAuthorizationResponseSuccessHandler;
import com.sven.auth.provider.CustomDaoAuthenticationProvider;
import com.sven.auth.provider.CustomOAuth2PasswordAuthorizationProvider;
import com.sven.auth.provider.CustomOAuth2SmsAuthorizationProvider;
import com.sven.auth.service.RedisOAuth2AuthorizationService;
import com.sven.auth.token.CustomeOAuth2TokenCustomizer;
import com.sven.auth.token.generator.CustomOAuth2AuthorizationCodeGenerator;
import com.sven.auth.token.generator.CustomeOAuth2AccessTokenGenerator;
import com.sven.common.constant.SecurityConstants;
import com.sven.common.security.UserInfo;

@Configuration
public class AuthorizationServerConfiguration {

    @Bean
    public SecurityFilterChain configure(HttpSecurity httpSecurity, OAuth2AuthorizationService authorizationService,
            RedisTemplate<String, Object> redisTemplate) throws Exception {
        // 短信登录验证码过滤器
        httpSecurity.addFilterBefore(new ValidateCodeFilter(redisTemplate), UsernamePasswordAuthenticationFilter.class);
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
        // oauth2.0的配置托管给 SpringSecurity
        httpSecurity.apply(authorizationServerConfigurer);
        
        authorizationServerConfigurer
            // 授权配置,处理/oauth2/authorize请求
            .authorizationEndpoint(authorizationEndpoint -> {
                    authorizationEndpoint
                            .authenticationProviders(authenticationProviders -> {
                                authenticationProviders.forEach(authenticationProvider -> {
                                    if (authenticationProvider instanceof OAuth2AuthorizationCodeRequestAuthenticationProvider) {
                                        // 无授权同意页面的授权码生成方式
                                        ((OAuth2AuthorizationCodeRequestAuthenticationProvider) authenticationProvider)
                                                .setAuthorizationCodeGenerator(
                                                        new CustomOAuth2AuthorizationCodeGenerator());
                                    }

                                    if (authenticationProvider instanceof OAuth2AuthorizationConsentAuthenticationProvider) {
                                        // 授权同意页面的授权码生成方式
                                        ((OAuth2AuthorizationConsentAuthenticationProvider) authenticationProvider)
                                                .setAuthorizationCodeGenerator(
                                                        new CustomOAuth2AuthorizationCodeGenerator());
                                    }
                                });
                            })
                            .authorizationResponseHandler(new CustomAuthorizationResponseSuccessHandler())
                            ;
            })
            // 令牌配置,处理/oauth2/token请求
            .tokenEndpoint(tokenEndpoint -> {
                // 自定义授权模式Token转换器
                tokenEndpoint.accessTokenRequestConverter(new CustomOAuth2PasswordAuthorizationConvert());
                tokenEndpoint.accessTokenRequestConverter(new CustomOAuth2SmsAuthorizationConvert());
            })
            // 令牌验签请求, 处理/oauth2/introspect请求
            .tokenIntrospectionEndpoint(Customizer.withDefaults())
            // 令牌撤销, 处理/oauth2/revoke请求
            .tokenRevocationEndpoint(Customizer.withDefaults())
            // OpenID配置
            .oidc(oidcConfigurer -> {
                oidcConfigurer
                    // 授权服务器的元数据配置, 处理/.well-known/openid-configuration请求
                    .providerConfigurationEndpoint(Customizer.withDefaults())
                    // 用户信息, 处理/userinfo请求
                    .userInfoEndpoint(userInfoEndpoint -> {
                        userInfoEndpoint
                            .userInfoRequestConverter(new CustomOidcUserInfoRequestConverter(authorizationService))
                            .userInfoMapper(context -> userInfoMapper(context))
                            ;
                    })
                ;
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

        // 针对UsernamePasswordAuthenticationToken的验证器
        httpSecurity.authenticationProvider(new CustomDaoAuthenticationProvider());
        // 用户密码授权模式验证器提供者, 组装成UsernamePasswordAuthenticationToken给CustomDaoAuthenticationProvider进行校验，成功后返回accessToken和refreshToken
        httpSecurity.authenticationProvider(customOAuth2PasswordAuthorizationProvider);
        // 短信授权模式验证器提供者, 组装成UsernamePasswordAuthenticationToken给CustomDaoAuthenticationProvider进行校验, 成功后返回accessToken和refreshToken
        httpSecurity.authenticationProvider(customOAuth2SmsAuthorizationProvider);
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 使用jdbc存储客户端信息
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }
    
    // 使用jdbc存储授权信息、TOKEN。需要对UserInfo配置Jackson序列化
//    @Bean
//    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
//            RegisteredClientRepository registeredClientRepository) {
//        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
//    }
    
    // 使用redis存储授权信息、TOKEN
    @Bean
    public OAuth2AuthorizationService authorizationService(RedisTemplate<String, Object> redisTemplate) {
        return new RedisOAuth2AuthorizationService(redisTemplate);
    }
    
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
            RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }
    
    // 自定义TOKEN的生成方式
    @Bean
    public OAuth2TokenGenerator<? extends OAuth2Token> oAuth2TokenGenerator() {
        CustomeOAuth2AccessTokenGenerator accessTokenGenerator = new CustomeOAuth2AccessTokenGenerator();
        // token增强
        accessTokenGenerator.setAccessTokenCustomizer(new CustomeOAuth2TokenCustomizer());
        // 提供accessToken、 refreshToken、openIdToken的生成方式, openIdToken一定得使用jwt生成方式
        return new DelegatingOAuth2TokenGenerator(accessTokenGenerator, new OAuth2RefreshTokenGenerator(), new JwtGenerator(jwtEncoder()));
    }
    
    // 改变所有默认端点的请求路径
    @Bean 
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
            .issuer("https://auth.server.com")      // 授权服务者的签发标识
            .build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        // 使用RSA密钥对
        RSAKey rsaKey = generateRsaKey();
        JWKSource<SecurityContext> jwkSource = (jwkSelector, securityContext) -> jwkSelector.select(new JWKSet(rsaKey));
        return new NimbusJwtEncoder(jwkSource);
    }

    private static RSAKey generateRsaKey() {
        KeyPair keyPair = generateRsaKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    private static KeyPair generateRsaKeyPair() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    private static OidcUserInfo userInfoMapper(OidcUserInfoAuthenticationContext contrxt) {
        Map<String, Object> claims = new HashMap<>();
        Authentication authentication = contrxt.getAuthentication();
        BearerTokenAuthentication bearerTokenAuthentication = (BearerTokenAuthentication) authentication.getPrincipal();
        UserInfo userInfo = (UserInfo) bearerTokenAuthentication.getPrincipal();
        claims.put(SecurityConstants.DETAILS_USER_ID, userInfo.getId());
        claims.put(SecurityConstants.USERNAME, userInfo.getUsername());
        return new OidcUserInfo(claims);
    }
}
