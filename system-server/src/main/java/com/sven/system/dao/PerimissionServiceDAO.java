package com.sven.system.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sven.common.dto.PerimissionDTO;
import com.sven.system.entity.PerimissionEntity;
import com.sven.system.mapper.PerimissionServiceMapper;

@Component
public class PerimissionServiceDAO extends ServiceImpl<PerimissionServiceMapper, PerimissionEntity> {

    public List<PerimissionEntity> selectList(PerimissionDTO dto) {
        LambdaQueryWrapper<PerimissionEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(dto.getName()), PerimissionEntity::getName, dto.getName());

        List<PerimissionEntity> perimissionInfoEntities = this.baseMapper.selectList(queryWrapper);

        return perimissionInfoEntities;
    }
    
    public int insert(PerimissionEntity entity) {
        return this.baseMapper.insert(entity);
    }

    public PerimissionEntity selectById(Long perimissionId) {
        return this.baseMapper.selectById(perimissionId);
    }
}
