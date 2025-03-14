package com.sven.system.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sven.common.dto.UserDTO;
import com.sven.system.entity.UserEntity;
import com.sven.system.mapper.UserServiceMapper;

@Component
public class UserServiceDAO extends ServiceImpl<UserServiceMapper, UserEntity> {

    public List<UserEntity> selectList(UserDTO dto) {
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(dto.getName()), UserEntity::getName, dto.getName());

        List<UserEntity> response = this.baseMapper.selectList(queryWrapper);

        return response;
    }

    public UserEntity selectOne(UserDTO dto) {
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNoneEmpty(dto.getName()), UserEntity::getName, dto.getName());
        queryWrapper.eq(StringUtils.isNoneEmpty(dto.getPhone()), UserEntity::getPhone, dto.getPhone());

        UserEntity response = this.baseMapper.selectOne(queryWrapper);

        return response;
    }

    public boolean saveBatch(List<UserEntity> userInfoEntities) {
        return this.saveBatch(userInfoEntities, DEFAULT_BATCH_SIZE);
    }
}
