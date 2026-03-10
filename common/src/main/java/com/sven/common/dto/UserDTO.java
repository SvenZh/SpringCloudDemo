package com.sven.common.dto;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.sven.common.domain.message.PageMessage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO extends PageMessage {

    private String name;
    
    private String phone;

    @NotEmpty(message = "密码不能为空")
    @Length(min = 8, message = "密码至少8位")
    private String password;
}
