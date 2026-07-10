package com.sven.system.service;

import java.util.List;
import java.util.Set;

import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.RolePermissionDTO;
import com.sven.common.vo.PermissionVO;

public interface IRolePermissionService {

    ResponseMessage<List<PermissionVO>> retrieveRolePermissionInfoByRoleId(Long roleId);

    ResponseMessage<Boolean> createRolePermission(RolePermissionDTO dto);

    ResponseMessage<Boolean> hasPermission(Set<String> authority, String requestPath);

}
