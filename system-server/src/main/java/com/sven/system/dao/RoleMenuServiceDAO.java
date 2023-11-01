package com.sven.system.dao;

import java.util.List;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sven.system.entity.RoleMenuEntity;
import com.sven.system.mapper.RoleMenuServiceMapper;

public class RoleMenuServiceDAO extends ServiceImpl<RoleMenuServiceMapper, RoleMenuEntity>{

    public List<RoleMenuEntity> retrieveRolePerimissionInfoByRoleId(Long roleId) {
        LambdaQueryWrapper<RoleMenuEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenuEntity::getRoleId, roleId);

        List<RoleMenuEntity> response = this.baseMapper.selectList(queryWrapper);
        
        return response;
    }

}
