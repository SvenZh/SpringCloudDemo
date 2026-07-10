package com.sven.auth.service;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dubbo.server.IUserService;
import com.sven.common.vo.UserVO;

import lombok.AllArgsConstructor;

@Primary
@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements CustomUserDetailsService {

    private final IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        ResponseMessage<UserVO> remoteResponse = userService.retrieveUserInfoByName(username);

        return getUserDetails(remoteResponse);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
