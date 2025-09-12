package com.sven.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sven.common.domain.message.IBaseResponseMessage;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.UserDTO;
import com.sven.common.vo.UserVO;
import com.sven.system.service.IUserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;
    
    @PreAuthorize("@pms.hasPermission('user.view')")
    @PostMapping("/list")
    public IBaseResponseMessage<List<UserVO>> retrieveRoleList(@RequestBody final UserDTO dto) {
        ResponseMessage<List<UserVO>> response = userService.retrieveUserList(dto);

        return response;
    }
    
    @PreAuthorize("@pms.hasPermission('user.view')")
    @PostMapping("/page")
    public IBaseResponseMessage<IPage<UserVO>> retrieveUserPage(@RequestBody @Validated final UserDTO dto) {
        ResponseMessage<IPage<UserVO>> response = userService.retrieveUserPage(dto);
        
        return response;
    }
    
    @PreAuthorize("@pms.hasPermission('user.add')")
    @PostMapping("/creation")
    public IBaseResponseMessage<Boolean> createUser(@RequestBody @Validated final List<UserDTO> dto) {
        ResponseMessage<Boolean> response = userService.createUser(dto);

        return response;
    }
}
