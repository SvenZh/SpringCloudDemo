package com.sven.common.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sven.common.domain.message.ErrorDetails;
import com.sven.common.domain.message.ResponseMessage;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SentinelBlockExceptionHandler implements BlockExceptionHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        // 记录日志
        log.warn("Sentinel BlockException: {} {}", request.getRequestURI(), e.getClass().getSimpleName());

        // 根据不同的异常返回不同的错误信息
        ResponseMessage<?> result;

        if (e instanceof FlowException) {
            result = new ResponseMessage<>(new ErrorDetails(429, "系统繁忙，请稍后再试"));
        } else if (e instanceof DegradeException) {
            result = new ResponseMessage<>(new ErrorDetails(503, "服务暂时不可用，请稍后重试"));
        } else if (e instanceof ParamFlowException) {
            result = new ResponseMessage<>(new ErrorDetails(429, "热点参数限流，请稍后重试"));
        } else if (e instanceof SystemBlockException) {
            result = new ResponseMessage<>(new ErrorDetails(429, "系统保护，请稍后再试"));
        } else if (e instanceof AuthorityException) {
            result = new ResponseMessage<>(new ErrorDetails(403, "无权限访问"));
        } else {
            result = new ResponseMessage<>(new ErrorDetails(429, "访问过于频繁，请稍后再试"));
        }

        // 设置响应
        response.setStatus(result.getCode());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // 写入响应体
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
