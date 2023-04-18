package com.sven.business.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sven.business.entity.UserInfoEntity;

@Mapper
public interface DemoServiceMapper extends BaseMapper<UserInfoEntity> {

}
