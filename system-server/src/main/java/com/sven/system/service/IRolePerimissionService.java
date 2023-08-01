package com.sven.system.service;

import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.RolePerimissionDTO;
import com.sven.common.vo.PerimissionInfoVO;
import com.sven.system.entity.RolePerimissionInfoEntity;

public interface IRolePerimissionService extends IService<RolePerimissionInfoEntity> {

    ResponseMessage<List<PerimissionInfoVO>> retrieveRolePerimissionInfoByRoleId(Long roleId);

    ResponseMessage<Boolean> createRolePerimission(RolePerimissionDTO dto);

    ResponseMessage<Boolean> hasPerimission(Set<String> authority, String requestPath);

}
