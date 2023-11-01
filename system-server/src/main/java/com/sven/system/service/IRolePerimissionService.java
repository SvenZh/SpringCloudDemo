package com.sven.system.service;

import java.util.List;
import java.util.Set;

import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.RolePerimissionDTO;
import com.sven.common.vo.PerimissionVO;

public interface IRolePerimissionService {

    ResponseMessage<List<PerimissionVO>> retrieveRolePerimissionInfoByRoleId(Long roleId);

    ResponseMessage<Boolean> createRolePerimission(RolePerimissionDTO dto);

    ResponseMessage<Boolean> hasPerimission(Set<String> authority, String requestPath);

}
