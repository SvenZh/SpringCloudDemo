package com.sven.system.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sven.common.dto.PermissionDTO;
import com.sven.system.entity.PermissionEntity;
import com.sven.system.mapper.PermissionServiceMapper;

@Component
public class PermissionServiceDAO extends ServiceImpl<PermissionServiceMapper, PermissionEntity> {

    public List<PermissionEntity> selectList(PermissionDTO dto) {
        LambdaQueryWrapper<PermissionEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(dto.getName()), PermissionEntity::getName, dto.getName());

        List<PermissionEntity> PermissionInfoEntities = this.baseMapper.selectList(queryWrapper);

        return PermissionInfoEntities;
    }
    
    public IPage<PermissionEntity> paging(PermissionDTO dto) {
        LambdaQueryWrapper<PermissionEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(dto.getName()), PermissionEntity::getName, dto.getName());
        
        return this.baseMapper.selectPage(Page.of(dto.getPageNo(), dto.getPageSize()), queryWrapper);
    }
    
    public int insert(PermissionEntity entity) {
        return this.baseMapper.insert(entity);
    }

    public PermissionEntity selectById(Long PermissionId) {
        return this.baseMapper.selectById(PermissionId);
    }
}
