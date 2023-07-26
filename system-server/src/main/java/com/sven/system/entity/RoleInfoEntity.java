package com.sven.system.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("sys_role")
public class RoleInfoEntity {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    
    private String name;
    
    private Integer sort;
    
    private Integer deleted;
    
    private Integer status;
    
    private Date createAt;
    
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createBy;
    
    private Date updateAt;
    
    @JsonSerialize(using = ToStringSerializer.class)
    private Long updateBy;
}
