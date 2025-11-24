package com.sven.common.security;


import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
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
                .sessionManagement(session -> session
                        // .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .sessionFixation().migrateSession()
                    .maximumSessions(1)
                    .maxSessionsPreventsLogin(false)
                )
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
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
                // 发起授权请求/oauth2/authorization/{registrationId}
                // 授权回调请求/login/oauth2/code/{registrationId}
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authorizationEndpoint -> 
                            // 自定义发起授权请求路径
                            authorizationEndpoint.baseUri("/oauth2/authorization")
                        )
                        // 登录页
                        .loginPage("/login")
                        // 登录成功后跳转页
                        .defaultSuccessUrl("/home", true)
                        // 登录失败后跳转页
                        .failureUrl("/error")
                        .userInfoEndpoint(userInfo -> userInfo
                                // .userService(oauth2UserService())
                                .oidcUserService(oidcUserService())
                        )
                )
                .logout(logout -> logout
                        // .logoutUrl("/logout")
                        // .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                )
                .exceptionHandling(exception -> exception.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/error")));
                ;

        return httpSecurity.build();
    }

    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        final OidcUserService delegate = new OidcUserService();
        return (userRequest) -> delegate.loadUser(userRequest);
    }

    // @Bean
    // public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
    //     DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    //     final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    //     return (userRequest) -> delegate.loadUser(userRequest);
    // }

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
