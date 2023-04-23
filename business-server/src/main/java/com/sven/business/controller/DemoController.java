package com.sven.business.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sven.business.dto.DemoDTO;
import com.sven.business.dto.UserInfoDTO;
import com.sven.business.service.IDemoService;
import com.sven.business.vo.UserInfoVO;
import com.sven.common.domain.message.IBaseResponseMessage;
import com.sven.common.domain.message.ResponseMessage;

@RestController
public class DemoController {
    @Autowired
    private IDemoService demoService;

    @PostMapping("/demo")
    public IBaseResponseMessage<IPage<UserInfoVO>> demo(@RequestBody @Validated DemoDTO body) {
        ResponseMessage<IPage<UserInfoVO>> response = demoService.getUserInfoList(body);
        
        return response;
    }
    
    @PostMapping("/demo2")
    public IBaseResponseMessage<Boolean> demo(@RequestBody @Validated List<UserInfoDTO> body) {
        ResponseMessage<Boolean> response = demoService.insertUser(body);

        return response;
    }
    
    @GetMapping("/demo3")
    public IBaseResponseMessage<?> demo() {
        ResponseMessage<String> response = demoService.getResponseFromAnotherSystem();
        
        return response;
    }
}
