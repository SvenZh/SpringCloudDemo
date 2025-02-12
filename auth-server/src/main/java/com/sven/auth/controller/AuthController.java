package com.sven.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sven.auth.service.AuthService;
import com.sven.auth.vo.CaptchVO;
import com.sven.common.domain.message.IBaseResponseMessage;
import com.sven.common.domain.message.ResponseMessage;

@RestController
@RequestMapping("/test")
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @GetMapping(value = "/captcha")
    public IBaseResponseMessage<CaptchVO> getCaptcha() {
        CaptchVO response = authService.getCaptcha();
        
        return ResponseMessage.ok(response);
    }
    
    @GetMapping(value = "/hello")
    public IBaseResponseMessage<String> hello() {
        
        return ResponseMessage.ok("hello");
    }
}
