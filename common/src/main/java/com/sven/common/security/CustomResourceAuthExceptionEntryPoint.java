package com.sven.common.security;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sven.common.domain.message.ErrorDetails;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.exception.BusinessExceptionEnum;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class CustomResourceAuthExceptionEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        ResponseMessage<String> result = new ResponseMessage<>();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        if (authException != null) {
            result.setError(new ErrorDetails(BusinessExceptionEnum.valid_exception));
        }

        // 令牌过期处理
        if (authException instanceof InvalidBearerTokenException
                || authException instanceof InsufficientAuthenticationException) {
            response.setStatus(HttpStatus.FAILED_DEPENDENCY.value());
            result.setError(new ErrorDetails(BusinessExceptionEnum.bearer_token_expire));
        }
        PrintWriter printWriter = response.getWriter();
        printWriter.append(objectMapper.writeValueAsString(result));
    }

}
