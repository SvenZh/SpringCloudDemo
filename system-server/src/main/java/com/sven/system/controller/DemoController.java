package com.sven.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sven.common.domain.message.IBaseResponseMessage;
import com.sven.common.domain.message.ResponseMessage;

@RestController
public class DemoController {

    @GetMapping("/ping")
    public IBaseResponseMessage<String> test() {

        ResponseMessage<String> response = new ResponseMessage<>("pong", 200);

        return response;
    }
}
