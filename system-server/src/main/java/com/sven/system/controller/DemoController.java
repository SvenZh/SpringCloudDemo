package com.sven.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sven.common.domain.message.IBaseResponseMessage;
import com.sven.common.domain.message.ResponseMessage;

@RestController
public class DemoController {
    
    @Autowired
    private Environment environment;
    
    @GetMapping("/ping")
    public IBaseResponseMessage<String> test() {
        String port = environment.getProperty("server.port");
        
        ResponseMessage<String> response = new ResponseMessage<>(port, 200);
        
        return response;
    }
}
