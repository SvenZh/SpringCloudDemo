package com.sven.auth.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sven.common.domain.message.ErrorDetails;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.exception.BusinessExceptionEnum;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GlobalAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        ResponseMessage<String> result = new ResponseMessage<>();
        result.setError(new ErrorDetails(BusinessExceptionEnum.authentication_exception));
        PrintWriter printWriter = response.getWriter();
        printWriter.append(objectMapper.writeValueAsString(result));
    }

}
