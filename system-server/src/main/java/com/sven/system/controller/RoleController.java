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
import com.sven.common.dto.RoleDTO;
import com.sven.common.vo.RoleVO;
import com.sven.system.service.IRoleService;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;
    
    @GetMapping("/retrieveRoleInfoByRoleId")
    public IBaseResponseMessage<RoleVO> retrieveRoleInfoByRoleId(@RequestParam("roleId") final Long roleId) {
        ResponseMessage<RoleVO> response = roleService.retrieveRoleInfoByRoleId(roleId);

        return response;
    }
    
    @GetMapping("/retrieveRoleInfoByRoleName")
    public IBaseResponseMessage<RoleVO> retrieveRoleInfoByRoleName(@RequestParam("roleName") final String roleName) {
        ResponseMessage<RoleVO> response = roleService.retrieveRoleInfoByRoleName(roleName);

        return response;
    }
    
    @PostMapping("/list")
    public IBaseResponseMessage<List<RoleVO>> retrieveRoleList(@RequestBody final RoleDTO dto) {
        ResponseMessage<List<RoleVO>> response = roleService.retrieveRoleList(dto);

        return response;
    }
    
    @PostMapping("/page")
    public IBaseResponseMessage<IPage<RoleVO>> retrieveRolePage(@RequestBody final RoleDTO dto) {
        ResponseMessage<IPage<RoleVO>> response = roleService.rolePage(dto);
        
        return response;
    }
    
    @PostMapping("/creation")
    public IBaseResponseMessage<Boolean> createRole(@RequestBody @Validated final RoleDTO dto) {
        ResponseMessage<Boolean> response = roleService.createRole(dto);
        
        return response;
    }
}
