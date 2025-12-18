package com.sven.common.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserVO implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    
    private String name;

    private String address;

    private String email;

    private String password;
    
    private String phone;
    
    private List<RoleVO> userRole;
    
    private List<PerimissionVO> userPerimission;
}
