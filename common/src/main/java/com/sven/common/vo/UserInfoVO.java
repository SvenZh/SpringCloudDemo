package com.sven.common.vo;

import java.util.Date;
import java.util.List;

import com.sven.common.domain.message.VoMessage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoVO extends VoMessage {

    private String name;

    private String address;

    private String email;

    private List<RoleInfoVO> userRole;

    private Date createAt;
}
