package com.sven.auth.convert;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import com.sven.auth.conf.SecurityConstants;
import com.sven.auth.token.CustomOAuth2SmsAuthorizationToken;
import com.sven.auth.utils.OAuth2EndpointUtils;

public class CustomOAuth2SmsAuthorizationConvert extends OAuth2BaseAuthorizationConvert {

    @Override
    public void checkParams(HttpServletRequest request) {
        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getQueryParameters(request);

        String phome = parameters.getFirst(SecurityConstants.PHONE);
        if (!StringUtils.hasText(phome) || parameters.get(SecurityConstants.PHONE).size() != 1) {
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, SecurityConstants.PHONE,
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }

        String code = parameters.getFirst(SecurityConstants.CODE);
        if (!StringUtils.hasText(code) || parameters.get(SecurityConstants.CODE).size() != 1) {
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, SecurityConstants.CODE,
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
    }

    @Override
    public boolean support(String grantType) {
        return SecurityConstants.GRANT_TYPE_SMS.equals(grantType);
    }

    @Override
    public Authentication tokenGenerator(Authentication principal, Set<String> scopes,
            Map<String, Object> additionalParameters) {
        return new CustomOAuth2SmsAuthorizationToken(new AuthorizationGrantType(SecurityConstants.GRANT_TYPE_SMS), principal, scopes,
                additionalParameters);
    }

}
