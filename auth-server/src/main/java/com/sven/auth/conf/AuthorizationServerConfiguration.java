package com.sven.auth.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.sven.auth.convert.CustomOAuth2PasswordAuthorizationConvert;
import com.sven.auth.provider.CustomOAuth2PasswordAuthorizationProvider;
import com.sven.auth.utils.OAuth2ConfigurerUtils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
public class AuthorizationServerConfiguration {
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    
    @Bean
    public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
        httpSecurity.apply(authorizationServerConfigurer.tokenEndpoint(tokenEndpoint -> {
            tokenEndpoint.accessTokenRequestConverter(new CustomOAuth2PasswordAuthorizationConvert());
        }));
        
        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        httpSecurity
            .requestMatcher(endpointsMatcher)
            .csrf(csrf -> csrf.disable())
            .formLogin(Customizer.withDefaults())
            .sessionManagement(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorizeRequests -> {
                    authorizeRequests.anyRequest().authenticated();
             })
            .headers(headers -> headers.cacheControl(cacheControl -> cacheControl.disable()))
            .oauth2ResourceServer(oauth2ResourceServer -> {
                oauth2ResourceServer.jwt(jwt -> jwt.decoder(jwtDecoder(jwkSource())));
            })
            ;
       
        addCustomOAuth2AuthenticationProvider(httpSecurity);
        
        return httpSecurity.build();
    }
    
    private void addCustomOAuth2AuthenticationProvider(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
        OAuth2AuthorizationService authorizationService = OAuth2ConfigurerUtils.getAuthorizationService(httpSecurity);
        OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator = OAuth2ConfigurerUtils.getTokenGenerator(httpSecurity);
        
        CustomOAuth2PasswordAuthorizationProvider customOAuth2PasswordAuthorizationProvider = new CustomOAuth2PasswordAuthorizationProvider(
                authenticationManager, authorizationService, tokenGenerator);

        httpSecurity.authenticationProvider(customOAuth2PasswordAuthorizationProvider);
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
//        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
//                .clientId("myClient")
//                .clientSecret(passwordEncoder.encode("123456"))
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
//                .redirectUri("https://www.baidu.com")
//                .scope("all")
//                .tokenSettings(TokenSettings.builder()
//                        .authorizationCodeTimeToLive(Duration.ofMinutes(30))
//                        .accessTokenTimeToLive(Duration.ofMinutes(30))
//                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
//                        .reuseRefreshTokens(true)
//                        .refreshTokenTimeToLive(Duration.ofMinutes(60))
//                        .build())
//                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
//                .build();
//        JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);
//        RegisteredClient repositoryByClientId = registeredClientRepository.findByClientId(registeredClient.getClientId());
//        if (repositoryByClientId == null) {
//            registeredClientRepository.save(registeredClient);
//        }
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    } 
    
    @Bean
    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
            RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
    }
    
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
            RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }
    
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User.withUsername("admin").password(passwordEncoder().encode("123456")).roles("admin")
                .build();
        return new InMemoryUserDetailsManager(userDetails);
    }
    
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }
    
    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }
    
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }
    
    @Bean 
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }
}
