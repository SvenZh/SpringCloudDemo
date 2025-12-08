package com.sven.common.vo;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerimissionVO implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    
    private String name;

    private String code;
    
    private Integer type;

    private String method;

    private String permission;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long menuId;
}
