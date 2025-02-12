package com.sven.auth.token;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.Assert;

import lombok.Getter;

public abstract class CustomOAuth2BaseAuthorizationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 1L;

    @Getter
    private final AuthorizationGrantType authorizationGrantType;

    private final Authentication principal;

    @Getter
    private final Set<String> scopes;

    @Getter
    private final Map<String, Object> additionalParameters;

    public CustomOAuth2BaseAuthorizationToken(AuthorizationGrantType authorizationGrantType,
            Authentication principal, @Nullable Set<String> scopes,
            @Nullable Map<String, Object> additionalParameters) {
        super(Collections.emptyList());
        Assert.notNull(authorizationGrantType, "authorizationGrantType cannot be null");
        Assert.notNull(principal, "principal cannot be null");
        this.authorizationGrantType = authorizationGrantType;
        this.principal = principal;
        this.scopes = Collections.unmodifiableSet(scopes != null ? new HashSet<>(scopes) : Collections.emptySet());
        this.additionalParameters = Collections.unmodifiableMap(
                additionalParameters != null ? new HashMap<>(additionalParameters) : Collections.emptyMap());
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
