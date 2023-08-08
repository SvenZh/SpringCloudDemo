package com.sven.common.domain.message;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseEntityMessage {
    
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    
    @TableLogic
    private Integer deleted;
    
    private Date createAt;
    
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createBy;
    
    private Date updateAt;
    
    @JsonSerialize(using = ToStringSerializer.class)
    private Long updateBy;
}
