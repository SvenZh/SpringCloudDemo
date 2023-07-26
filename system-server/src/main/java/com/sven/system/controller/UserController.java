package com.sven.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sven.common.domain.message.IBaseResponseMessage;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.UserInfoDTO;
import com.sven.common.vo.UserInfoVO;
import com.sven.system.service.IUserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;
    
    @PostMapping("/creation")
    public IBaseResponseMessage<Boolean> creation(@RequestBody @Validated List<UserInfoDTO> dto) {
        ResponseMessage<Boolean> response = userService.creation(dto);

        return response;
    }
    
    @PostMapping("/page")
    public IBaseResponseMessage<IPage<UserInfoVO>> userPage(@RequestBody @Validated UserInfoDTO dto) {
        ResponseMessage<IPage<UserInfoVO>> response = userService.userPage(dto);
        
        return response;
    }
    
    @GetMapping("/retrieveUserInfoByName")
    public IBaseResponseMessage<UserInfoVO> retrieveUserInfoByName(@RequestParam("userName") String userName) {
        ResponseMessage<UserInfoVO> response = userService.retrieveUserInfoByName(userName);

        return response;
    }
}
