package com.sven.common.security;

import java.lang.reflect.Method;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomOAuth2AccessTokenInterceptor implements RequestInterceptor {

    private final BearerTokenResolver tokenResolver;

    @Override
    public void apply(RequestTemplate template) {
        Method method = template.methodMetadata().method();
        NoToken noToken = method.getAnnotation(NoToken.class);

        if (noToken != null) {
            return;
        }

        if (!getRequest().isPresent()) {
            return;
        }
        HttpServletRequest request = getRequest().get();

        String token = tokenResolver.resolve(request);
        if (StringUtils.isBlank(token)) {
            return;
        }
        template.header(HttpHeaders.AUTHORIZATION,
                String.format("%s %s", OAuth2AccessToken.TokenType.BEARER.getValue(), token));
    }

    private Optional<HttpServletRequest> getRequest() {
        ServletRequestAttributes servletRequestAttributes = getServletRequestAttributes();
        if (servletRequestAttributes == null) {
            return Optional.empty();
        }
        return Optional.of(servletRequestAttributes.getRequest());
    }

    private ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

}
