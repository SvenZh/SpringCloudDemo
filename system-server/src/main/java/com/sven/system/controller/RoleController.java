package com.sven.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sven.common.domain.message.IBaseResponseMessage;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.RoleInfoDTO;
import com.sven.common.vo.RoleInfoVO;
import com.sven.system.service.IRoleService;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;
    
    @PostMapping("/list")
    public IBaseResponseMessage<List<RoleInfoVO>> retrieveRoleList(@RequestBody RoleInfoDTO dto) {
        ResponseMessage<List<RoleInfoVO>> response = roleService.retrieveRoleList(dto);

        return response;
    }
    
    @PostMapping("/page")
    public IBaseResponseMessage<IPage<RoleInfoVO>> retrieveRolePage(@RequestBody RoleInfoDTO dto) {
        ResponseMessage<IPage<RoleInfoVO>> response = roleService.rolePage(dto);
        
        return response;
    }
    
    @PostMapping("/creation")
    public IBaseResponseMessage<Boolean> createRole(@RequestBody @Validated RoleInfoDTO dto) {
        ResponseMessage<Boolean> response = roleService.createRole(dto);
        
        return response;
    }
}
