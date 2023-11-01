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
@TableName("sys_menu")
public class MenuEntity extends BaseEntityMessage{

    private String name;
    
    private Integer sort;
    
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parent_id;
    
    private String icon;
    
    private String path;
}
