package com.sven.service.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sven.service.dto.DemoDTO;
import com.sven.service.entity.UserInfoEntity;
import com.sven.service.vo.UserInfoVO;

public interface IDemoService extends IService<UserInfoEntity> {
    public IPage<UserInfoVO> getUserInfoList(DemoDTO body);
}
