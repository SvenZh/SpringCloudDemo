package com.sven.auth.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sven.common.constant.AppConstant;
import com.sven.common.constant.SecurityConstants;

import cn.hutool.core.util.StrUtil;

@Component
public class ValidateCodeFilter extends OncePerRequestFilter {

    private RedisTemplate<String, Object> redisTemplate;
    
    public ValidateCodeFilter(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestUrl = request.getServletPath();

        if (!SecurityConstants.OAUTH_TOKEN_URL.equals(requestUrl)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (StrUtil.equals(SecurityConstants.REFRESH_TOKEN, grantType)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        if (StrUtil.equals(SecurityConstants.AUTHORIZATION_CODE, grantType)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        if (StrUtil.equals(SecurityConstants.PASSWORD_TOKEN, grantType)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (StrUtil.equals(SecurityConstants.CLIENT_CREDENTIALS, grantType)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            checkCode();
            filterChain.doFilter(request, response);
        }
        catch (Exception ex) {
            throw new OAuth2AuthenticationException(ex.getMessage());
        }
    }
    
    private void checkCode() throws Exception {
        ServletRequestAttributes request = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String code = request.getRequest().getParameter("code");
        String phone = request.getRequest().getParameter("phone");

        String key = AppConstant.VALIDATION_CODE_PREFIX + phone;
        Object cacheCode = redisTemplate.opsForValue().get(key);

        if (!code.equals(cacheCode)) {
            throw new RuntimeException("验证码错误");
        }

        redisTemplate.delete(key);
    }

}
