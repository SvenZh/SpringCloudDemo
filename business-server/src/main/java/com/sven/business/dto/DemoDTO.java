package com.sven.business.dto;

import javax.validation.constraints.NotEmpty;

import com.sven.common.domain.message.PageMessage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DemoDTO extends PageMessage {

    @NotEmpty(message = "不能为空")
    public String name;
}
