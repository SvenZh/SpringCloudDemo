package com.sven.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sven.common.domain.message.BaseEntityMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_role")
public class RoleInfoEntity extends BaseEntityMessage {
    private String name;

    private Integer sort;

    private Integer status;
}
