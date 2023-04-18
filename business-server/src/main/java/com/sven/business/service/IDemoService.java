package com.sven.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sven.business.dto.DemoDTO;
import com.sven.business.entity.UserInfoEntity;
import com.sven.business.vo.UserInfoVO;
import com.sven.common.domain.message.ResponseMessage;

public interface IDemoService extends IService<UserInfoEntity> {
    public ResponseMessage<IPage<UserInfoVO>> getUserInfoList(DemoDTO body);

    public ResponseMessage<String> getResponseFromAnotherSystem();
}
