package com.sven.system.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sven.system.entity.UserRoleEntity;
import com.sven.system.mapper.UserRoleServiceMapper;

@Component
public class UserRoleServiceDAO extends ServiceImpl<UserRoleServiceMapper, UserRoleEntity> {

    public List<UserRoleEntity> selectList(Long userId) {
        LambdaQueryWrapper<UserRoleEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRoleEntity::getUserId, userId);

        List<UserRoleEntity> response = this.baseMapper.selectList(queryWrapper);

        return response;
    }

    public int insert(UserRoleEntity userRoleInfoEntity) {
        return this.baseMapper.insert(userRoleInfoEntity);
    }
}
