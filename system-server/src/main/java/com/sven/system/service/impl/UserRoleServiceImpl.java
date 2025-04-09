package com.sven.system.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.UserRoleCreationDTO;
import com.sven.common.vo.RoleVO;
import com.sven.system.dao.UserRoleServiceDAO;
import com.sven.system.entity.UserRoleEntity;
import com.sven.system.service.IRoleService;
import com.sven.system.service.IUserRoleService;

@Service
public class UserRoleServiceImpl implements IUserRoleService {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private UserRoleServiceDAO userRoleServiceDAO;

    @Override
    public ResponseMessage<List<RoleVO>> retrieveUserRoleInfoByUserId(final Long userId) {

        List<UserRoleEntity> userRoleInfoEntities = userRoleServiceDAO.selectList(userId);

        List<RoleVO> response = userRoleInfoEntities.stream().map(entity -> {
            ResponseMessage<RoleVO> result = roleService.retrieveRoleInfoByRoleId(entity.getRoleId());
            if (result.isSuccess()) {
                return result.getData();
            }

            throw new RuntimeException("找不到角色!");
        }).collect(Collectors.toList());

        return ResponseMessage.ok(response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseMessage<Boolean> creationUserRole(final UserRoleCreationDTO dto) {
        dto.getUserIds().parallelStream().forEach(userId -> {
            List<UserRoleEntity> userRoleEntities = dto.getRoleIds().stream()
                    .map(roleId -> UserRoleEntity.builder().userId(userId).roleId(roleId).build())
                    .collect(Collectors.toList());

            userRoleServiceDAO.saveBatch(userRoleEntities);
        });

        return ResponseMessage.ok(true);
    }
}
