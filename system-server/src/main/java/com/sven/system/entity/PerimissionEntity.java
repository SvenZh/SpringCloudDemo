package com.sven.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.sven.common.domain.message.BaseEntityMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_permission")
public class PerimissionEntity extends BaseEntityMessage {
    private String name;

    private String code;
    
    private Integer type;

    private String method;

    private String permission;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long menuId;
}
