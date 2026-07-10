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
import com.sven.common.dto.RolePermissionDTO;
import com.sven.common.vo.PermissionVO;
import com.sven.system.service.IRolePermissionService;

@RestController
@RequestMapping("/rolePermission")
public class RolePermissionController {

    @Autowired
    private IRolePermissionService rolePermissionService;

    @GetMapping("/retrieveRolePermissionInfoByRoleId")
    public IBaseResponseMessage<List<PermissionVO>> retrieveRolePermissionInfoByRoleId(
            @RequestParam("roleId") final Long roleId) {
        ResponseMessage<List<PermissionVO>> response = rolePermissionService
                .retrieveRolePermissionInfoByRoleId(roleId);

        return response;
    }

    @PostMapping("/creation")
    public IBaseResponseMessage<Boolean> createrolePermission(@RequestBody @Validated final RolePermissionDTO dto) {
        ResponseMessage<Boolean> response = rolePermissionService.createRolePermission(dto);

        return response;
    }
    
    @GetMapping("/hasPermission")
    public IBaseResponseMessage<Boolean> hasPermission(@RequestParam("authority") final Set<String> authority,
            @RequestParam("requestPath") final String requestPath) {
        ResponseMessage<Boolean> response = rolePermissionService.hasPermission(authority, requestPath);

        return response;
    }
}
