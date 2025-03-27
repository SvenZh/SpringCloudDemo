package com.sven.common.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.sven.common.domain.message.PageMessage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerimissionDTO extends PageMessage {

    @NotEmpty(message = "不能为空")
    private String name;

    @NotEmpty(message = "不能为空")
    private String code;
    
    private Integer type = 1;
    
    @NotEmpty(message = "不能为空")
    private String method;
    
    @NotEmpty(message = "不能为空")
    private String permission;
    
    @NotNull(message = "不能为空")
    private Long menuId;
    
    private Integer sort = 1;

    private Integer deleted = 0;

    private Integer status = 0;
}
