package com.sven.system.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.RoleInfoDTO;
import com.sven.common.vo.RoleInfoVO;
import com.sven.system.entity.RoleInfoEntity;

public interface IRoleService extends IService<RoleInfoEntity> {

    ResponseMessage<List<RoleInfoVO>> retrieveRoleList(final RoleInfoDTO dto);

    ResponseMessage<IPage<RoleInfoVO>> rolePage(final RoleInfoDTO dto);

    ResponseMessage<Boolean> createRole(final RoleInfoDTO dto);

    ResponseMessage<RoleInfoVO> retrieveRoleInfoByRoleId(final Long roleId);

    ResponseMessage<RoleInfoVO> retrieveRoleInfoByRoleName(final String roleName);
}
