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
import com.sven.common.dto.PermissionDTO;
import com.sven.common.vo.PermissionVO;
import com.sven.system.service.IPermissionService;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private IPermissionService permissionService;
    
    @PostMapping("/list")
    public IBaseResponseMessage<List<PermissionVO>> retrievePermissionList(@RequestBody final PermissionDTO dto) {
        ResponseMessage<List<PermissionVO>> response = permissionService.retrievePermissionList(dto);

        return response;
    }
    
    @PostMapping("/page")
    public IBaseResponseMessage<IPage<PermissionVO>> retrieveRolePage(@RequestBody final PermissionDTO dto) {
        ResponseMessage<IPage<PermissionVO>> response = permissionService.retrievePermissionPage(dto);
        
        return response;
    }
    
    @PreAuthorize("@pms.hasPermission('permission.add')")
    @PostMapping("/creation")
    public IBaseResponseMessage<Boolean> createPermission(@RequestBody @Validated final PermissionDTO dto) {
        ResponseMessage<Boolean> response = permissionService.createPermission(dto);
        
        return response;
    }
}
