package com.sven.system.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.UserInfoDTO;
import com.sven.common.vo.UserInfoVO;
import com.sven.system.entity.UserInfoEntity;

public interface IUserService extends IService<UserInfoEntity> {

    ResponseMessage<UserInfoVO> retrieveUserInfoByName(String userName);

    ResponseMessage<Boolean> creation(List<UserInfoDTO> dto);

    ResponseMessage<IPage<UserInfoVO>> userPage(UserInfoDTO dto);
}
