package com.sven.auth.handler;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.sven.auth.service.UserInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthorizationResponseSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        OAuth2AuthorizationCodeRequestAuthenticationToken accessTokenAuthentication = (OAuth2AuthorizationCodeRequestAuthenticationToken) authentication;
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) accessTokenAuthentication.getPrincipal();
        UserInfo userInfo = (UserInfo) usernamePasswordAuthenticationToken.getPrincipal();
        log.info(userInfo.getUsername() + "登录成功, 授权码是: " + accessTokenAuthentication.getAuthorizationCode().getTokenValue());
    }

}
