package com.sven.common.vo;

import com.sven.common.domain.message.VoMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleInfoVO extends VoMessage {
    
    private String name;
    
    private Integer sort;
    
    private Integer deleted;
}
