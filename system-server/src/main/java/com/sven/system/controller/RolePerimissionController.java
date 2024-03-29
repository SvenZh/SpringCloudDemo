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
import com.sven.common.dto.RolePerimissionDTO;
import com.sven.common.vo.PerimissionVO;
import com.sven.system.service.IRolePerimissionService;

@RestController
@RequestMapping("/rolePerimission")
public class RolePerimissionController {

    @Autowired
    private IRolePerimissionService rolePerimissionService;

    @GetMapping("/retrieveRolePerimissionInfoByRoleId")
    public IBaseResponseMessage<List<PerimissionVO>> retrieveRolePerimissionInfoByRoleId(
            @RequestParam("roleId") final Long roleId) {
        ResponseMessage<List<PerimissionVO>> response = rolePerimissionService
                .retrieveRolePerimissionInfoByRoleId(roleId);

        return response;
    }

    @PostMapping("/creation")
    public IBaseResponseMessage<Boolean> createRolePerimission(@RequestBody @Validated final RolePerimissionDTO dto) {
        ResponseMessage<Boolean> response = rolePerimissionService.createRolePerimission(dto);

        return response;
    }
    
    @GetMapping("/hasPerimission")
    public IBaseResponseMessage<Boolean> hasPerimission(@RequestParam("authority") final Set<String> authority,
            @RequestParam("requestPath") final String requestPath) {
        ResponseMessage<Boolean> response = rolePerimissionService.hasPerimission(authority, requestPath);

        return response;
    }
}
