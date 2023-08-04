package com.sven.system.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.UserRoleCreationDTO;
import com.sven.common.vo.RoleInfoVO;
import com.sven.system.entity.UserRoleInfoEntity;
import com.sven.system.mapper.UserRoleServiceMapper;
import com.sven.system.service.IRoleService;
import com.sven.system.service.IUserRoleService;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleServiceMapper, UserRoleInfoEntity>
        implements IUserRoleService {

    @Autowired
    private IRoleService roleService;

    @Override
    public ResponseMessage<List<RoleInfoVO>> retrieveUserRoleInfoByUserId(final Long userId) {

        LambdaQueryWrapper<UserRoleInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRoleInfoEntity::getUserId, userId);

        List<UserRoleInfoEntity> userRoleInfoEntities = this.baseMapper.selectList(queryWrapper);

        List<RoleInfoVO> response = userRoleInfoEntities.stream().map(entity -> {
            ResponseMessage<RoleInfoVO> result = roleService.retrieveRoleInfoByRoleId(entity.getRoleId());
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
            dto.getRoleIds().stream().forEach(roleId -> {
                UserRoleInfoEntity userRoleInfoEntity = new UserRoleInfoEntity();
                userRoleInfoEntity.setUserId(userId);
                userRoleInfoEntity.setRoleId(roleId);

                this.baseMapper.insert(userRoleInfoEntity);
            });
        });

        return ResponseMessage.ok(true);
    }
}
