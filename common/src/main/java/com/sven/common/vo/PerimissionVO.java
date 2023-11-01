package com.sven.common.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerimissionVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    
    private String name;

    private Integer type;

    private String method;

    private String permission;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long menuId;
}
