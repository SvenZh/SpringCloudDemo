package com.sven.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/seata-commit")
    public void seataTestConnmit() {
        testServiceImpl.seataTestCommit();
    }

    @GetMapping("/seata-rollback")
    public void seataTestRollback() {
        testServiceImpl.seataTestRollback();
    }
}
