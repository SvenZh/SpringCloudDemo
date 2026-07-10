package com.sven.business.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sven.business.service.TestServiceImpl;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/test")
@AllArgsConstructor
public class SeateTestController {

    private final TestServiceImpl testServiceImpl;

    @GetMapping("/seata-commit")
    public void seataTestConnmit() {
        testServiceImpl.seataTestCommit();
    }

    @GetMapping("/seata-rollback")
    public void seataTestRollback() {
        testServiceImpl.seataTestRollback();
    }
}
