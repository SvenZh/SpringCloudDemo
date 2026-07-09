package com.sven.business.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.sven.business.service.TestServiceImpl;
import com.sven.common.security.NoToken;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestServiceImpl testServiceImpl;

    @NoToken
    @GetMapping("/hello")
    public String test() {
        return "hello oauth2";
    }

    @NoToken
    @GetMapping("/hello/v2")
    @SentinelResource(value = "/test/hello/v2")
    public String test(@RequestParam("name") String name) {
        return "hello " + name;
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

    @GetMapping("/seata-commit")
    public void seataTestConnmit() {
        testServiceImpl.seataTestCommit();
    }

    @GetMapping("/seata-rollback")
    public void seataTestRollback() {
        testServiceImpl.seataTestRollback();
    }
}
