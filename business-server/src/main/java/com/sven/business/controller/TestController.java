package com.sven.business.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sven.common.security.NoToken;

@RestController
@RequestMapping("/test")
public class TestController {

    @NoToken
    @GetMapping("/hello")
    public String test() {
        return "hello oauth2";
    }
}
