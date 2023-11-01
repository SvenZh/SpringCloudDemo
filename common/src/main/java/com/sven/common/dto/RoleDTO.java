package com.sven.common.dto;

import javax.validation.constraints.NotEmpty;

import com.sven.common.domain.message.PageMessage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDTO extends PageMessage {

    private Long id;
    
    @NotEmpty(message = "不能为空")
    private String name;
    
    private Integer sort = 1;

    private Integer deleted = 0;

    private Integer status = 0;
}
