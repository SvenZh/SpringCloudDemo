package com.sven.system.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.UserDTO;
import com.sven.common.vo.UserVO;

public interface IUserService {

    ResponseMessage<UserVO> retrieveUserInfoByName(String userName);

    ResponseMessage<Boolean> createUser(List<UserDTO> dto);

    ResponseMessage<IPage<UserVO>> retrieveUserPage(UserDTO dto);

    ResponseMessage<List<UserVO>> retrieveUserList(UserDTO dto);
}
