package com.sven.common.constant;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.sven.common.security.UserInfo;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserContextHolder {

    public UserInfo getUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        return getUser(authentication);
    }

    public UserInfo getUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserInfo) {
            return (UserInfo) principal;
        }
        return null;
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
