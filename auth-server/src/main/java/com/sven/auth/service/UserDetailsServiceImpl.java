package com.sven.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.feign.client.SystemServerFeignClient;
import com.sven.common.vo.UserVO;

@Primary
@Service
public class UserDetailsServiceImpl implements CustomUserDetailsService {

    @Autowired
    private SystemServerFeignClient systemServerFeignClient;

    @Override
    public UserDetails loadUserByUsername(String username) {
        ResponseMessage<UserVO> remoteResponse = systemServerFeignClient.retrieveUserInfoByName(username);

        return getUserDetails(remoteResponse);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
