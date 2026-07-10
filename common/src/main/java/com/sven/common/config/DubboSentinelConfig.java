package com.sven.common.config;

import javax.annotation.PostConstruct;

import org.apache.dubbo.rpc.AsyncRpcResult;
import org.springframework.context.annotation.Configuration;

import com.alibaba.csp.sentinel.adapter.dubbo3.config.DubboAdapterGlobalConfig;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class DubboSentinelConfig {
    @PostConstruct
    public void init() {
        // 自定义 Provider 端的 Fallback
        DubboAdapterGlobalConfig.setProviderFallback((invoker, invocation, ex) -> {
            log.warn("Dubbo Provider 限流: 方法={}, 异常={}, 来源={}",
                    invocation.getMethodName(),
                    ex.getClass().getSimpleName(),
                    DubboAdapterGlobalConfig.getOriginParser().parse(invoker, invocation));

            String errorMessage = getErrorMessage(ex);
            return AsyncRpcResult.newDefaultAsyncResult(new RuntimeException(errorMessage), invocation);
        });

        // 自定义 Consumer 端的 Fallback
        DubboAdapterGlobalConfig.setConsumerFallback((invoker, invocation, ex) -> {
            log.warn("Dubbo Consumer 限流: 方法={}, 异常={}", 
                    invocation.getMethodName(), 
                    ex.getClass().getSimpleName());

            String errorMessage = getErrorMessage(ex);
            return AsyncRpcResult.newDefaultAsyncResult(new RuntimeException(errorMessage), invocation);
        });

        log.info("Dubbo Sentinel Fallback 配置完成");
    }

    private String getErrorMessage(BlockException ex) {
        if (ex instanceof FlowException) {
            return "系统繁忙，请稍后再试";
        } else if (ex instanceof ParamFlowException) {
            return "热点参数限流，请稍后重试";
        } else if (ex instanceof SystemBlockException) {
            return "系统保护，请稍后再试";
        } else {
            return "访问过于频繁，请稍后再试";
        }
    }
}
