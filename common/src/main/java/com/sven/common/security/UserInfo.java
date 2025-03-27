package com.sven.common.security;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import lombok.Getter;

@Getter
public class UserInfo extends User implements OAuth2AuthenticatedPrincipal {

    private final Map<String, Object> attributes = new HashMap<>();
    
    private static final long serialVersionUID = -4427480555371949046L;

    private Long id;

    private String phone;

    public UserInfo(Long id, String username, String password, String phone,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
        this.phone = phone;
    }
    
    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getName() {
        return this.getUsername();
    }
}
