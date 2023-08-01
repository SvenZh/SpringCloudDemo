package com.sven.gateway.filter;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.feign.client.SystemServerFeignClient;
import com.sven.gateway.porp.OAuthProperties;
import com.sven.gateway.provider.OAuthProvider;
import com.sven.gateway.provider.RequestProvider;
import com.sven.gateway.provider.ResponseProvider;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class OAuthFilter implements GlobalFilter, Ordered {
    @Autowired
    @Qualifier("jwtTokenStore")
    private TokenStore tokenStore;

    @Autowired
    private SystemServerFeignClient systemServerFeignClient;
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String originalRequestUrl = RequestProvider.getOriginalRequestUrl(exchange);
        String path = exchange.getRequest().getURI().getPath();
        if (isSkip(path) || isSkip(originalRequestUrl)) {
            return chain.filter(exchange);
        }

        ServerHttpResponse resp = exchange.getResponse();
        String headerToken = exchange.getRequest().getHeaders().getFirst(OAuthProvider.AUTH_KEY);
        String paramToken = exchange.getRequest().getQueryParams().getFirst(OAuthProvider.AUTH_KEY);
        if (StringUtils.isAllBlank(headerToken, paramToken)) {
            return unAuth(resp, "缺失令牌,鉴权失败", HttpStatus.UNAUTHORIZED);
        }
        String token = StringUtils.isBlank(headerToken) ? paramToken : headerToken;

        try {
            OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
            boolean expired = oAuth2AccessToken.isExpired();
            if (expired) {
                return unAuth(resp, "认证令牌已过期", HttpStatus.UNAUTHORIZED);
            }
            
            OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(oAuth2AccessToken);
            Collection<GrantedAuthority> grantedAuthority = oAuth2Authentication.getAuthorities();
            
            Set<String> authority = grantedAuthority.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
            
            ResponseMessage<Boolean> hasPerimission = systemServerFeignClient.hasPerimission(authority, path);
            
            if (!hasPerimission.isSuccess() || !hasPerimission.getData()) {
                return unAuth(resp, "无权访问", HttpStatus.FORBIDDEN);
            }
            
        } catch (InvalidTokenException e) {
            log.info("认证令牌无效: {}", token);
            return unAuth(resp, "认证令牌无效", HttpStatus.UNAUTHORIZED);
        }

        return chain.filter(exchange);
    }

    private boolean isSkip(String path) {
        return ifExistOAuthProvider(path) || ifExistOAuthProperties(path);
    }

    private boolean ifExistOAuthProvider(String path) {
        return OAuthProvider.getDefaultSkipUrl().stream()
                .map(url -> url.replace(OAuthProvider.TARGET, OAuthProvider.REPLACEMENT))
                .anyMatch(path::startsWith);
    }
    
    private boolean ifExistOAuthProperties(String path) {
        OAuthProperties oAuthProperties = new OAuthProperties();
        
        return oAuthProperties.getSkipUrl().stream()
                .map(url -> url.replace(OAuthProvider.TARGET, OAuthProvider.REPLACEMENT))
                .anyMatch(path::startsWith);
    }
    
    private Mono<Void> unAuth(ServerHttpResponse resp, String msg, HttpStatus httpStatus) {
        resp.setStatusCode(httpStatus);
        resp.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        String result = "";
        try {
            result = new ObjectMapper().writeValueAsString(ResponseProvider.unAuth(httpStatus.value(), msg));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        DataBuffer buffer = resp.bufferFactory().wrap(result.getBytes(StandardCharsets.UTF_8));
        
        return resp.writeWith(Flux.just(buffer));
    }

    @Override
    public int getOrder() {
        return 10200;
    }
}