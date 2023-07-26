package com.sven.common.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.sven.common.domain.message.PageMessage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolePerimissionDTO extends PageMessage {
    @NotEmpty(message = "不能为空")
    private List<Long> perimissionIds;

    @NotEmpty(message = "不能为空")
    private List<Long> roleIds;
}
