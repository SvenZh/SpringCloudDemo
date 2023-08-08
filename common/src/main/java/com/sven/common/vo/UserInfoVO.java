package com.sven.common.vo;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    
    private String name;

    private String address;

    private String email;

    private String password;
    
    private List<RoleInfoVO> userRole;
}
