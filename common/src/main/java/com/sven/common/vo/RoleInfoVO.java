package com.sven.common.vo;

import java.util.Date;

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
public class RoleInfoVO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    
    private String name;
    
    private Integer sort;
    
    private Integer deleted;
    
    private Date createAt;
    
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createBy;
    
    private Date updateAt;
    
    @JsonSerialize(using = ToStringSerializer.class)
    private Long updateBy;
}
