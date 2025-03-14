package com.sven.auth.conf;

import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import com.sven.auth.vo.UserInfo;

public class CustomeOAuth2TokenCustomizer implements OAuth2TokenCustomizer<OAuth2TokenClaimsContext>{

    @Override
    public void customize(OAuth2TokenClaimsContext context) {
        OAuth2TokenClaimsSet.Builder claims = context.getClaims();
        String clientId = context.getAuthorizationGrant().getName();
        claims.claim(SecurityConstants.CLIENT_ID, clientId);
        // 客户端模式不返回具体用户信息
        if (SecurityConstants.CLIENT_CREDENTIALS.equals(context.getAuthorizationGrantType().getValue())) {
            return;
        }

        UserInfo userInfo = (UserInfo) context.getPrincipal().getPrincipal();
        claims.claim(SecurityConstants.DETAILS_USER, userInfo);
        claims.claim(SecurityConstants.DETAILS_USER_ID, userInfo.getId());
        claims.claim(SecurityConstants.USERNAME, userInfo.getUsername());
    }

}
