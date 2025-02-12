package com.sven.auth.token;

import java.util.Map;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

public class CustomOAuth2PasswordAuthorizationToken extends CustomOAuth2BaseAuthorizationToken {

    private static final long serialVersionUID = 1L;

    public CustomOAuth2PasswordAuthorizationToken(AuthorizationGrantType authorizationGrantType,
            Authentication principal, Set<String> scopes, Map<String, Object> additionalParameters) {
        super(authorizationGrantType, principal, scopes, additionalParameters);
    }

}
