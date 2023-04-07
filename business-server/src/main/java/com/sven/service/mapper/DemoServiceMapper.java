package com.sven.service.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sven.service.entity.UserInfoEntity;

@Mapper
public interface DemoServiceMapper extends BaseMapper<UserInfoEntity> {

}
