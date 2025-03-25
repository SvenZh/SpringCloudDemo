package com.sven.common.vo;

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
public class RoleVO {
    
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    
    private String name;
    
    private String code;
    
    private Integer sort;
}
