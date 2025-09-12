package com.sven.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sven.common.domain.message.IBaseResponseMessage;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.security.NoToken;
import com.sven.common.vo.UserVO;
import com.sven.system.service.IUserService;

@RestController
@RequestMapping("/internal")
public class SysInternalController {
    @Autowired
    private IUserService userService;

    @NoToken
    @GetMapping("/retrieveUserInfoByName")
    public IBaseResponseMessage<UserVO> retrieveUserInfoByName(@RequestParam("userName") final String userName) {
        ResponseMessage<UserVO> response = userService.retrieveUserInfoByName(userName);

        return response;
    }

    @NoToken
    @GetMapping("/retrieveUserInfoByphone")
    public IBaseResponseMessage<UserVO> retrieveUserInfoByphone(@RequestParam("phone") final String phone) {
        ResponseMessage<UserVO> response = userService.retrieveUserInfoByPhone(phone);

        return response;
    }

    @NoToken
    @GetMapping("/sms")
    public IBaseResponseMessage<Boolean> sms(@RequestParam("phone") final String phone) {
        ResponseMessage<Boolean> response = userService.sms(phone);

        return response;
    }
}
