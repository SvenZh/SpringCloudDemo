package com.sven.auth.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sven.auth.vo.UserInfo;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.feign.client.SystemServerFeignClient;
import com.sven.common.vo.RoleInfoVO;
import com.sven.common.vo.UserInfoVO;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private SystemServerFeignClient systemServerFeignClient;

    @Override
    public UserDetails loadUserByUsername(String username) {
        ResponseMessage<UserInfoVO> remoteResponse = systemServerFeignClient.retrieveUserInfoByName(username);

        if (!remoteResponse.isSuccess()) {
            throw new UsernameNotFoundException(username);
        }

        UserInfoVO userInfoVO = remoteResponse.getData();

        Long userId = userInfoVO.getId();
        String password = userInfoVO.getPassword();
        List<RoleInfoVO> userRole = userInfoVO.getUserRole();
        List<String> role = userRole.stream().map(RoleInfoVO::getName).collect(Collectors.toList());
        
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(role.toArray(new String[0]));

        return new UserInfo(userId, username, password, authorities);
    }
}
