package com.sven.common.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleCreationDTO {
    @NotEmpty(message = "不能为空")
    private List<Long> userIds;
    
    @NotEmpty(message = "不能为空")
    private List<Long> roleIds;
}
