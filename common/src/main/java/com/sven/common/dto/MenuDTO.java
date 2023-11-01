package com.sven.common.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuDTO {
    @NotEmpty(message = "不能为空")
    private String roleName;
}
