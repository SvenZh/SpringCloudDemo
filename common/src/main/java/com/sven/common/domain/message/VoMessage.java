package com.sven.common.domain.message;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoMessage {
    
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    
    private Integer deleted;

    private Date createAt;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long createBy;

    private Date updateAt;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long updateBy;
}
