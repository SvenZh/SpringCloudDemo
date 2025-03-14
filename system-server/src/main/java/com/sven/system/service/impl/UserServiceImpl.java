package com.sven.system.service.impl;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.UserDTO;
import com.sven.common.vo.RoleVO;
import com.sven.common.vo.UserVO;
import com.sven.system.dao.UserServiceDAO;
import com.sven.system.entity.UserEntity;
import com.sven.system.service.IUserRoleService;
import com.sven.system.service.IUserService;

@Service
@DS("master")
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private UserServiceDAO userServiceDAO;
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public ResponseMessage<List<UserVO>> retrieveUserList(final UserDTO dto) {

        List<UserEntity> userInfoEntities = userServiceDAO.selectList(dto);

        List<UserVO> response = userInfoEntities.stream().map(entity -> {
            UserVO vo = new UserVO();
            BeanUtils.copyProperties(entity, vo);

            return vo;
        }).collect(Collectors.toList());

        return ResponseMessage.ok(response);
    }

    @Override
    public ResponseMessage<UserVO> retrieveUserInfoByName(final String userName) {

        UserVO response = new UserVO();

        UserDTO dto = new UserDTO();
        dto.setName(userName);
        UserEntity userInfoEntity = userServiceDAO.selectOne(dto);

        BeanUtils.copyProperties(userInfoEntity, response);

        ResponseMessage<List<RoleVO>> roleInfo = userRoleService.retrieveUserRoleInfoByUserId(response.getId());

        if (roleInfo.isSuccess()) {
            response.setUserRole(roleInfo.getData());
        }

        return ResponseMessage.ok(response);
    }
    
    @Override
    public ResponseMessage<UserVO> retrieveUserInfoByPhone(final String phone) {
        
        UserVO response = new UserVO();
        
        UserDTO dto = new UserDTO();
        dto.setPhone(phone);
        UserEntity userInfoEntity = userServiceDAO.selectOne(dto);
        
        BeanUtils.copyProperties(userInfoEntity, response);
        
        ResponseMessage<List<RoleVO>> roleInfo = userRoleService.retrieveUserRoleInfoByUserId(response.getId());
        
        if (roleInfo.isSuccess()) {
            response.setUserRole(roleInfo.getData());
        }
        
        return ResponseMessage.ok(response);
    }
    
    @Override
    public ResponseMessage<Boolean> sms(final String phone) {
        
        redisTemplate.opsForValue().set(phone, "123", Duration.ofMinutes(5));
        
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
        List<UserEntity> result = userServiceDAO.selectList(dto);

        List<UserVO> response = result.stream().map(entity -> {
            UserVO vo = new UserVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        }).collect(Collectors.toList());

        Page<UserVO> page = Page.of(dto.getPageNo(), dto.getPageSize(), response.size());
        page.setRecords(response);

        return ResponseMessage.ok(page);
    }
}
