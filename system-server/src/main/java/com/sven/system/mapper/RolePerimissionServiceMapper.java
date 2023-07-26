package com.sven.system.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sven.system.entity.RolePerimissionInfoEntity;

@Mapper
public interface RolePerimissionServiceMapper extends BaseMapper<RolePerimissionInfoEntity> {

}
