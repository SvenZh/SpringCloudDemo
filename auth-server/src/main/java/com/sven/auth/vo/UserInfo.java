package com.sven.auth.vo;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class UserInfo extends User {

    private static final long serialVersionUID = -4427480555371949046L;

    private Long id;

    private String phone;

    public UserInfo(Long id, String username, String password, String phone,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
        this.phone = phone;
    }
}
