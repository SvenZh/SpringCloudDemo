package com.sven.system.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.UserRoleCreationDTO;
import com.sven.common.vo.RoleInfoVO;
import com.sven.system.entity.UserRoleInfoEntity;

public interface IUserRoleService extends IService<UserRoleInfoEntity> {

    ResponseMessage<List<RoleInfoVO>> retrieveUserRoleInfoByUserId(final Long userId);

    ResponseMessage<Boolean> creationUserRole(final UserRoleCreationDTO dto);
}
