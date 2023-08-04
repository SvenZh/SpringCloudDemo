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

import com.sven.common.domain.message.IBaseResponseMessage;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.UserRoleCreationDTO;
import com.sven.common.vo.RoleInfoVO;
import com.sven.system.service.IUserRoleService;

@RestController
@RequestMapping("/userRole")
public class UserRoleController {

    @Autowired
    private IUserRoleService userRoleService;

    @GetMapping("/retrieveUserRoleInfoByUserId")
    public IBaseResponseMessage<List<RoleInfoVO>> retrieveUserRoleInfoByUserId(@RequestParam("userId") final Long userId) {
        ResponseMessage<List<RoleInfoVO>> response = userRoleService.retrieveUserRoleInfoByUserId(userId);

        return response;
    }

    @PostMapping("/creation")
    public IBaseResponseMessage<Boolean> creationUserRole(@RequestBody @Validated final UserRoleCreationDTO dto) {
        ResponseMessage<Boolean> response = userRoleService.creationUserRole(dto);

        return response;
    }
}
