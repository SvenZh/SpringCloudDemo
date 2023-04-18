package com.sven.gateway.filter;


import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.sven.gateway.provider.RequestProvider;

import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String originalRequestUrl = RequestProvider.getOriginalRequestUrl(exchange);
        String path = exchange.getRequest().getURI().getPath();

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 10200;
    }
}
