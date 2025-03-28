package com.sven.auth.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.core.Ordered;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.security.UserInfo;
import com.sven.common.vo.PerimissionVO;
import com.sven.common.vo.RoleVO;
import com.sven.common.vo.UserVO;

public interface CustomUserDetailsService extends UserDetailsService, Ordered {

    default boolean support(String clientId, String grantType) {
        return true;
    }
    
    default UserDetails getUserDetails(ResponseMessage<UserVO> remoteResponse) {
        
        if (!remoteResponse.isSuccess()) {
            throw new UsernameNotFoundException("用户不存在");
        }

        UserVO userInfoVO = remoteResponse.getData();
        
        List<RoleVO> userRole = userInfoVO.getUserRole();
        List<PerimissionVO> userPerimissions = userInfoVO.getUserPerimission();
        
        Set<String> role = userRole.stream().map(RoleVO::getCode).collect(Collectors.toSet());
        Set<String> perimission = userPerimissions.stream().map(PerimissionVO::getCode).collect(Collectors.toSet());
        role.addAll(perimission);
        
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(role.toArray(new String[0]));

        return new UserInfo(userInfoVO.getId(), userInfoVO.getName(), userInfoVO.getPassword(), userInfoVO.getPhone(), authorities);
    }
    
    default UserDetails loadUserByUser(UserInfo user) {
        return this.loadUserByUsername(user.getUsername());
    }   
}
