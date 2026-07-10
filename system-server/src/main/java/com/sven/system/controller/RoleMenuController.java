package com.sven.system.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sven.common.domain.message.IBaseResponseMessage;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.MenuDTO;
import com.sven.common.vo.MenuVO;
import com.sven.system.service.IRoleMenuService;

@RestController
@RequestMapping("/roleMenu")
public class RoleMenuController {

    @Autowired
    private IRoleMenuService roleMenuService;
    
    @PostMapping("/retrieveRolePermissionInfoByRoleId")
    public IBaseResponseMessage<List<MenuVO>> retrieveRolePermissionInfoByRoleId(@RequestParam("roleId") final Long roleId) {
        ResponseMessage<List<MenuVO>> response = roleMenuService.retrieveRoleMenuInfoByRoleId(roleId);

        return response;
    }
    
    @PostMapping("/creation")
    public IBaseResponseMessage<Boolean> createPermission(@RequestBody @Validated final MenuDTO dto) {
        ResponseMessage<Boolean> response = roleMenuService.createMenu(dto);
        
        return response;
    }
    
    @GetMapping("/hasPermission")
    public IBaseResponseMessage<Boolean> hasPermission(@RequestParam("authority") final Set<String> authority,
            @RequestParam("requestPath") final String requestPath) {
        ResponseMessage<Boolean> response = roleMenuService.hasPermission(authority, requestPath);

        return response;
    }
}
