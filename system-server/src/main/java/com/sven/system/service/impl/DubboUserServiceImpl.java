package com.sven.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.UserDTO;
import com.sven.common.dubbo.server.IUserService;
import com.sven.common.exception.BusinessExceptionEnum;
import com.sven.common.vo.PerimissionVO;
import com.sven.common.vo.RoleVO;
import com.sven.common.vo.UserVO;
import com.sven.system.dao.UserServiceDAO;
import com.sven.system.entity.UserEntity;
import com.sven.system.service.IRolePerimissionService;
import com.sven.system.service.IUserRoleService;

import lombok.extern.slf4j.Slf4j;

@Service("dubboUserService")
@DS("master")
@Slf4j
public class DubboUserServiceImpl implements IUserService {

    @Autowired
    private UserServiceDAO userServiceDAO;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRolePerimissionService rolePerimissionService;

    @Override
    @SentinelResource(value = "retrieveUserInfoByName")
    public ResponseMessage<UserVO> retrieveUserInfoByName(String userName) {
        UserDTO dto = new UserDTO();
        dto.setName(userName);

        return ResponseMessage.ok(retrieveUserInfo(dto));
    }

    private UserVO retrieveUserInfo(UserDTO dto) {
        UserVO response = new UserVO();
        UserEntity userInfoEntity = userServiceDAO.selectOne(dto);
        BusinessExceptionEnum.user_not_found.assertNotNull(userInfoEntity);
        BeanUtils.copyProperties(userInfoEntity, response);
        ResponseMessage<List<RoleVO>> roleInfo = userRoleService.retrieveUserRoleInfoByUserId(response.getId());

        if (!roleInfo.isSuccess()) {
            return response;
        }
       
        List<PerimissionVO> userPerimission = roleInfo.getData().stream().flatMap(role -> {
            ResponseMessage<List<PerimissionVO>> perimissionVO = rolePerimissionService
                    .retrieveRolePerimissionInfoByRoleId(role.getId());
            
            return Optional.ofNullable(perimissionVO.getData()).orElseGet(() -> new ArrayList<>()).stream();
        }).collect(Collectors.toList());

        response.setUserRole(roleInfo.getData());
        response.setUserPerimission(userPerimission);

        return response;
    }

}
