package com.sven.system.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sven.common.dto.RolePermissionDTO;
import com.sven.system.entity.RolePermissionEntity;
import com.sven.system.mapper.rolePermissionServiceMapper;

@Component
public class RolePermissionServiceDAO extends ServiceImpl<rolePermissionServiceMapper, RolePermissionEntity> {

    public List<RolePermissionEntity> selectList(RolePermissionDTO dto) {
        LambdaQueryWrapper<RolePermissionEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RolePermissionEntity::getRoleId, dto.getRoleId());

        List<RolePermissionEntity> response = this.baseMapper.selectList(queryWrapper);

        return response;
    }

    public int insert(RolePermissionEntity rolePermissionInfoEntity) {
        return this.baseMapper.insert(rolePermissionInfoEntity);
    }
}
