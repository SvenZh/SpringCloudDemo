package com.sven.common.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.sven.common.domain.message.PageMessage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolePermissionDTO extends PageMessage {
    @NotNull(message = "不能为空")
    private List<Long> permissionIds;

    @NotNull(message = "不能为空")
    private Long roleId;
}
