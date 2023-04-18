package com.sven.business.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.sven.common.domain.message.PageMessage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DemoDTO extends PageMessage {

    @NotNull(message = "不能为空")
    @Length(max = 1, message = "超过最大长度")
    public String testValid;
}
