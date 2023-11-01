package com.sven.system.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.RoleDTO;
import com.sven.common.vo.RoleVO;

public interface IRoleService {

    ResponseMessage<List<RoleVO>> retrieveRoleList(final RoleDTO dto);

    ResponseMessage<IPage<RoleVO>> rolePage(final RoleDTO dto);

    ResponseMessage<Boolean> createRole(final RoleDTO dto);

    ResponseMessage<RoleVO> retrieveRoleInfoByRoleId(final Long roleId);

    ResponseMessage<RoleVO> retrieveRoleInfoByRoleName(final String roleName);
}
