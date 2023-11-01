package com.sven.system.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sven.common.dto.RolePerimissionDTO;
import com.sven.system.entity.RolePerimissionEntity;
import com.sven.system.mapper.RolePerimissionServiceMapper;

@Component
public class RolePerimissionServiceDAO extends ServiceImpl<RolePerimissionServiceMapper, RolePerimissionEntity> {

    public List<RolePerimissionEntity> selectList(RolePerimissionDTO dto) {
        LambdaQueryWrapper<RolePerimissionEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RolePerimissionEntity::getRoleId, dto.getRoleId());

        List<RolePerimissionEntity> response = this.baseMapper.selectList(queryWrapper);

        return response;
    }

    public int insert(RolePerimissionEntity rolePerimissionInfoEntity) {
        return this.baseMapper.insert(rolePerimissionInfoEntity);
    }
}
