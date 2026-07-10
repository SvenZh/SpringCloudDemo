package com.sven.business.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowArgument;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.sven.business.service.TestServiceImpl;
import com.sven.common.security.NoToken;

import lombok.Getter;
import lombok.Setter;

@RestController
@RequestMapping("/test")
public class SentinelTestController {

    @Autowired
    private TestServiceImpl testServiceImpl;

    /**
     * 流控规则限流：基于HTTP路径，实现BlockExceptionHandler自定义限流异常处理
     * @return
     */
    @NoToken
    @GetMapping("/hello")
    public String test() {
        return "hello oauth2";
    }

    /**
     * 热点规则限流：必须使用@SentinelResource注解
     * @param name
     * @return
     */
    @NoToken
    @GetMapping("/hello/v2")
    @SentinelResource(value = "/test/hello/v2")
    public String test(@RequestParam("name") String name) {
        return "hello " + name;
    }

    /**
     * 热点规则限流：@RequestBody注解的实体类
     * @param name
     * @return
     */
    @NoToken
    @PostMapping("/hello/v3")
    @SentinelResource(value = "/test/hello/v3")
    public String test(@RequestBody ParamFlowArg paramFlowArg) {
        return "hello " + paramFlowArg.getName();
    }
    
    @NoToken
    @GetMapping("/rules/param")
    public List<ParamFlowRule> getParamRules() {
        return ParamFlowRuleManager.getRules();
    }

    @NoToken
    @GetMapping("/rules/flow")
    public List<FlowRule> getFlowRules() {
        return FlowRuleManager.getRules();
    }

    @Getter
    @Setter
    public static class ParamFlowArg implements ParamFlowArgument {
        private Long id;
        private String name;

        @Override
        public Object paramFlowKey() {
            return this.name;
        }
    }

}
