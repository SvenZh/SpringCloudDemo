package com.sven.common.dto;

import com.sven.common.domain.message.PageMessage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO extends PageMessage {

    private String name;
    
    private String phone;
}
