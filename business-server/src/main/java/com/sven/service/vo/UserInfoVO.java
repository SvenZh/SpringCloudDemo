package com.sven.service.vo;

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
}
