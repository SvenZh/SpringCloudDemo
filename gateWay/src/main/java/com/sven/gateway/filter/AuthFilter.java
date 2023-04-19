package com.sven.gateway.filter;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sven.gateway.porp.AuthProperties;
import com.sven.gateway.provider.AuthProvider;
import com.sven.gateway.provider.RequestProvider;
import com.sven.gateway.provider.ResponseProvider;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    @Autowired
    @Qualifier("jwtTokenStore")
    private TokenStore tokenStore;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String originalRequestUrl = RequestProvider.getOriginalRequestUrl(exchange);
        String path = exchange.getRequest().getURI().getPath();
        if (isSkip(path) || isSkip(originalRequestUrl)) {
            return chain.filter(exchange);
        }
        
        ServerHttpResponse resp = exchange.getResponse();
        String headerToken = exchange.getRequest().getHeaders().getFirst(AuthProvider.AUTH_KEY);
        String paramToken = exchange.getRequest().getQueryParams().getFirst(AuthProvider.AUTH_KEY);
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
        } catch (InvalidTokenException e) {
            log.info("认证令牌无效: {}", token);
            return unAuth(resp, "认证令牌无效", HttpStatus.UNAUTHORIZED);
        }
        
        return chain.filter(exchange);
    }
    
    private boolean isSkip(String path) {
        AuthProperties authProperties = new AuthProperties();
        
        return AuthProvider.getDefaultSkipUrl().stream().map(url -> url.replace(AuthProvider.TARGET, AuthProvider.REPLACEMENT)).anyMatch(path::startsWith)
            || authProperties.getSkipUrl().stream().map(url -> url.replace(AuthProvider.TARGET, AuthProvider.REPLACEMENT)).anyMatch(path::startsWith);
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
