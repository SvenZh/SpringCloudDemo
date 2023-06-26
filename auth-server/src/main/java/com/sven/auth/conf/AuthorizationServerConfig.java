package com.sven.auth.conf;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import com.google.common.collect.Lists;
import com.sven.auth.service.UserService;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    @Qualifier("jdbcClientDetailsService")
    private ClientDetailsService jdbcClientDetailsService;
    
    @Autowired
    @Qualifier("tokenServices")
    private AuthorizationServerTokenServices authorizationServerTokenServices;
    
    @Autowired
    @Qualifier("jdbcAuthorizationCodeServices")
    private AuthorizationCodeServices authorizationCodeServers;
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("isAuthenticated()")
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(jdbcClientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authorizationCodeServices(authorizationCodeServers)            
                .authenticationManager(authenticationManager)                   
                .tokenServices(authorizationServerTokenServices)                
                .userDetailsService(userService)
                .allowedTokenEndpointRequestMethods(HttpMethod.POST, HttpMethod.GET)
                .tokenGranter(tokenGranter(endpoints))
                ;
    }

    private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
        List<TokenGranter> tokenGranters = Lists.newArrayList(endpoints.getTokenGranter());
        tokenGranters.add(new CaptchaTokenGranter(authenticationManager, authorizationServerTokenServices,
                jdbcClientDetailsService, endpoints.getOAuth2RequestFactory(), redisTemplate));

        return new CompositeTokenGranter(tokenGranters);
    }
}
