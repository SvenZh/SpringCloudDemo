package com.sven.auth.convert;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

import com.sven.common.security.UserInfo;

public class CustomOidcUserInfoRequestConverter implements AuthenticationConverter {

    private final OAuth2AuthorizationService authorizationService;

    public CustomOidcUserInfoRequestConverter(OAuth2AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
        }
        String token = authorizationHeader.substring(7);

        OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        Authentication principal = authorization.getAttribute(Principal.class.getName());
        OAuth2AccessToken accessToken = authorization.getAccessToken().getToken();

        // 重新创建 OAuth2AccessToken，确保正确的TokenType
        OAuth2AccessToken bearerAccessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                accessToken.getTokenValue(),
                accessToken.getIssuedAt(),
                accessToken.getExpiresAt(),
                accessToken.getScopes());

        BearerTokenAuthentication bearerTokenAuthentication = new BearerTokenAuthentication(
                (UserInfo) principal.getPrincipal(),
                bearerAccessToken,
                principal.getAuthorities());

        return new OidcUserInfoAuthenticationToken(bearerTokenAuthentication);
    }
}
