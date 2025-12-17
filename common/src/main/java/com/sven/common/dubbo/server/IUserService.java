package com.sven.common.dubbo.server;

import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.vo.UserVO;

public interface IUserService {
    public ResponseMessage<UserVO> retrieveUserInfoByName(final String userName);

     public ResponseMessage<Void> addUser(final String userName);
}
