package com.sven.system.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
public class UserInfoEntity {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    
    private String name;
    
    private String password;
    
    private String address;
    
    private String email;
    
    @TableLogic
    private Integer deleted;
    
    private Date createAt;
    
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createBy;
    
    private Date updateAt;
    
    @JsonSerialize(using = ToStringSerializer.class)
    private Long updateBy;
}
