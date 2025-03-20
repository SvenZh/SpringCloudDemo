package com.sven.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.feign.client.SystemServerFeignClient;
import com.sven.common.vo.UserVO;

@Service
public class UserAppDetailsServiceImpl implements CustomUserDetailsService {

    @Autowired
    private SystemServerFeignClient systemServerFeignClient;

    @Override
    public UserDetails loadUserByUsername(String phone) {
        ResponseMessage<UserVO> remoteResponse = systemServerFeignClient.retrieveUserInfoByPhone(phone);

        return getUserDetails(remoteResponse);
    }

    @Override
    public UserDetails loadUserByUser(UserInfo UserInfo) {
        return this.loadUserByUsername(UserInfo.getPhone());
    }

    @Override
    public boolean support(String clientId, String grantType) {
        return SecurityConstants.GRANT_TYPE_SMS.equals(grantType);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
