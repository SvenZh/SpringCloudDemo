package com.sven.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sven.common.domain.message.BaseEntityMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
public class UserEntity extends BaseEntityMessage {

    private String name;

    private String password;
    
    private String phone;

    private String address;

    private String email;
}
