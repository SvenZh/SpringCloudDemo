package com.sven.business.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sven.business.dto.DemoDTO;
import com.sven.business.dto.UserInfoDTO;
import com.sven.business.entity.UserInfoEntity;
import com.sven.business.vo.UserInfoVO;
import com.sven.common.domain.message.ResponseMessage;

public interface IDemoService extends IService<UserInfoEntity> {
    ResponseMessage<IPage<UserInfoVO>> getUserInfoList(DemoDTO body);

    ResponseMessage<String> getResponseFromAnotherSystem();

    ResponseMessage<Boolean> insertUser(List<UserInfoDTO> body);
}
