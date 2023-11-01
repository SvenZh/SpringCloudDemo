package com.sven.system.service;

import java.util.List;

import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.UserRoleCreationDTO;
import com.sven.common.vo.RoleVO;

public interface IUserRoleService {

    ResponseMessage<List<RoleVO>> retrieveUserRoleInfoByUserId(final Long userId);

    ResponseMessage<Boolean> creationUserRole(final UserRoleCreationDTO dto);
}
