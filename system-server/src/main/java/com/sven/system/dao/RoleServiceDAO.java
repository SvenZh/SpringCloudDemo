package com.sven.system.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.alibaba.nacos.common.utils.Objects;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sven.common.dto.RoleDTO;
import com.sven.system.entity.RoleEntity;
import com.sven.system.mapper.RoleServiceMapper;

@Component
public class RoleServiceDAO extends ServiceImpl<RoleServiceMapper, RoleEntity> {

    public List<RoleEntity> selectList(RoleDTO dto) {
        LambdaQueryWrapper<RoleEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(dto.getName()), RoleEntity::getName, dto.getName());

        List<RoleEntity> response = this.baseMapper.selectList(queryWrapper);

        return response;
    }
    
    public RoleEntity selectOne(RoleDTO dto) {
        LambdaQueryWrapper<RoleEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(!Objects.isNull(dto.getId()), RoleEntity::getId, dto.getId());
        queryWrapper.eq(StringUtils.isNotEmpty(dto.getName()), RoleEntity::getName, dto.getName());
        
        RoleEntity response = this.baseMapper.selectOne(queryWrapper);
        
        return response;
    }

    public int insert(RoleEntity entity) {
        return this.baseMapper.insert(entity);
    }
}
