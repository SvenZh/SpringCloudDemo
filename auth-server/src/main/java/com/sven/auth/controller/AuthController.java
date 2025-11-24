package com.sven.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sven.auth.service.AuthService;
import com.sven.auth.vo.CaptchVO;
import com.sven.common.domain.message.IBaseResponseMessage;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.RegClientDTO;
import com.sven.common.security.NoToken;

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
    public IBaseResponseMessage<String> hello(@RequestParam("type") String type) {
        return ResponseMessage.ok("hello");
    }
    
    @NoToken
    @PostMapping(value = "/reg")
    public IBaseResponseMessage<Boolean> registeredClient(@RequestBody @Validated RegClientDTO dto) {
        authService.registeredClient(dto);  
        return ResponseMessage.ok(true);
         
    }
}
