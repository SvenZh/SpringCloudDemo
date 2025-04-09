package com.sven.system.service.impl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sven.common.constant.AppConstant;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.UserDTO;
import com.sven.common.exception.BusinessExceptionEnum;
import com.sven.common.vo.PerimissionVO;
import com.sven.common.vo.RoleVO;
import com.sven.common.vo.UserVO;
import com.sven.system.dao.UserServiceDAO;
import com.sven.system.entity.UserEntity;
import com.sven.system.service.IRolePerimissionService;
import com.sven.system.service.IUserRoleService;
import com.sven.system.service.IUserService;

@Service
@DS("master")
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRolePerimissionService rolePerimissionService;
    
    @Autowired
    private UserServiceDAO userServiceDAO;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public ResponseMessage<List<UserVO>> retrieveUserList(final UserDTO dto) {

        List<UserEntity> userInfoEntities = userServiceDAO.selectList(dto);

        List<UserVO> response = userInfoEntities.stream().map(entity -> {
            UserVO vo = new UserVO();
            BeanUtils.copyProperties(entity, vo);
            ResponseMessage<List<RoleVO>> roleInfo = userRoleService.retrieveUserRoleInfoByUserId(vo.getId());

            if (!roleInfo.isSuccess()) {
                return vo;
            }
           
            List<PerimissionVO> userPerimission = roleInfo.getData().stream().flatMap(role -> {
                ResponseMessage<List<PerimissionVO>> perimissionVO = rolePerimissionService
                        .retrieveRolePerimissionInfoByRoleId(role.getId());
                
                return Optional.ofNullable(perimissionVO.getData()).orElseGet(() -> new ArrayList<>()).stream();
            }).collect(Collectors.toList());

            vo.setUserRole(roleInfo.getData());
            vo.setUserPerimission(userPerimission);
            
            return vo;
        }).collect(Collectors.toList());
        
        return ResponseMessage.ok(response);
    }

    @Override
    public ResponseMessage<UserVO> retrieveUserInfoByName(final String userName) {
        UserDTO dto = new UserDTO();
        dto.setName(userName);

        return ResponseMessage.ok(retrieveUserInfo(dto));
    }
    
    @Override
    public ResponseMessage<UserVO> retrieveUserInfoByPhone(final String phone) {
        UserDTO dto = new UserDTO();
        dto.setPhone(phone);
        
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
    
    
    @Override
    public ResponseMessage<Boolean> sms(final String phone) {
        
        redisTemplate.opsForValue().set(AppConstant.VALIDATION_CODE_PREFIX + phone, "123", Duration.ofMinutes(5));
        
        return ResponseMessage.ok(true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseMessage<Boolean> createUser(final List<UserDTO> dto) {
        List<UserEntity> userInfoEntities = dto.stream().map(item -> {
            UserEntity userInfoEntity = new UserEntity();
            BeanUtils.copyProperties(item, userInfoEntity);

            return userInfoEntity;
        }).collect(Collectors.toList());

        boolean response = userServiceDAO.saveBatch(userInfoEntities);

        return ResponseMessage.ok(response);
    }

    @Override
    public ResponseMessage<IPage<UserVO>> retrieveUserPage(final UserDTO dto) {
        IPage<UserEntity> result = userServiceDAO.paging(dto);
        
        IPage<UserVO> response = result.convert(entity -> {
            UserVO vo = new UserVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        });

        return ResponseMessage.ok(response);
    }
}
