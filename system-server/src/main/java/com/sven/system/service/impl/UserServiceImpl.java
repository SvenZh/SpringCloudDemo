package com.sven.system.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.UserInfoDTO;
import com.sven.common.vo.RoleInfoVO;
import com.sven.common.vo.UserInfoVO;
import com.sven.system.entity.UserInfoEntity;
import com.sven.system.mapper.UserServiceMapper;
import com.sven.system.service.IUserRoleService;
import com.sven.system.service.IUserService;

@Service
@DS("master")
public class UserServiceImpl extends ServiceImpl<UserServiceMapper, UserInfoEntity> implements IUserService {

    @Autowired
    private IUserRoleService userRoleService;

    @Override
    public ResponseMessage<List<UserInfoVO>> retrieveUserList(final UserInfoDTO dto) {
        LambdaQueryWrapper<UserInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(dto.getName()), UserInfoEntity::getName, dto.getName());

        List<UserInfoEntity> userInfoEntities = this.baseMapper.selectList(queryWrapper);

        List<UserInfoVO> response = userInfoEntities.stream().map(entity -> {
            UserInfoVO vo = new UserInfoVO();
            BeanUtils.copyProperties(entity, vo);

            return vo;
        }).collect(Collectors.toList());

        return ResponseMessage.ok(response);
    }

    @Override
    public ResponseMessage<UserInfoVO> retrieveUserInfoByName(final String userName) {

        UserInfoVO response = new UserInfoVO();

        LambdaQueryWrapper<UserInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfoEntity::getName, userName);

        UserInfoEntity userInfoEntity = this.baseMapper.selectOne(queryWrapper);

        BeanUtils.copyProperties(userInfoEntity, response);

        ResponseMessage<List<RoleInfoVO>> roleInfo = userRoleService.retrieveUserRoleInfoByUserId(response.getId());

        if (roleInfo.isSuccess()) {
            response.setUserRole(roleInfo.getData());
        }

        return ResponseMessage.ok(response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseMessage<Boolean> createUser(final List<UserInfoDTO> dto) {
        List<UserInfoEntity> userInfoEntities = dto.stream().map(item -> {
            UserInfoEntity userInfoEntity = new UserInfoEntity();
            BeanUtils.copyProperties(item, userInfoEntity);

            return userInfoEntity;
        }).collect(Collectors.toList());

        boolean response = this.saveBatch(userInfoEntities, DEFAULT_BATCH_SIZE);

        return ResponseMessage.ok(response);
    }

    @Override
    public ResponseMessage<IPage<UserInfoVO>> retrieveUserPage(final UserInfoDTO dto) {
        List<UserInfoEntity> result = this.getBaseMapper().selectList(new QueryWrapper<>());

        List<UserInfoVO> response = result.stream().map(entity -> {
            UserInfoVO vo = new UserInfoVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        }).collect(Collectors.toList());

        Page<UserInfoVO> page = Page.of(dto.getPageNo(), dto.getPageSize(), response.size());
        page.setRecords(response);

        return ResponseMessage.ok(page);
    }
}
