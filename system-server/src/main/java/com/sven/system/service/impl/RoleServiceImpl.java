package com.sven.system.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.RoleInfoDTO;
import com.sven.common.vo.RoleInfoVO;
import com.sven.system.entity.RoleInfoEntity;
import com.sven.system.mapper.RoleServiceMapper;
import com.sven.system.service.IRoleService;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleServiceMapper, RoleInfoEntity> implements IRoleService {

    @Override
    public ResponseMessage<List<RoleInfoVO>> retrieveRoleList(RoleInfoDTO dto) {

        LambdaQueryWrapper<RoleInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(dto.getName()), RoleInfoEntity::getName, dto.getName());

        List<RoleInfoEntity> roleInfoEntities = this.baseMapper.selectList(queryWrapper);

        List<RoleInfoVO> response = roleInfoEntities.stream().map(entity -> {
            RoleInfoVO vo = new RoleInfoVO();
            BeanUtils.copyProperties(entity, vo);

            return vo;
        }).collect(Collectors.toList());

        return ResponseMessage.ok(response);
    }

    @Override
    public ResponseMessage<IPage<RoleInfoVO>> rolePage(RoleInfoDTO dto) {

        LambdaQueryWrapper<RoleInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        List<RoleInfoEntity> roleInfoEntities = this.baseMapper.selectList(queryWrapper);

        List<RoleInfoVO> response = roleInfoEntities.stream().map(entity -> {
            RoleInfoVO vo = new RoleInfoVO();
            BeanUtils.copyProperties(entity, vo);

            return vo;
        }).collect(Collectors.toList());

        Page<RoleInfoVO> page = Page.of(dto.getPageNo(), dto.getPageSize(), response.size());
        page.setRecords(response);

        return ResponseMessage.ok(page);
    }

    @Override
    public ResponseMessage<Boolean> createRole(RoleInfoDTO dto) {
        RoleInfoEntity entity = new RoleInfoEntity();
        entity.setCreateAt(new Date());
        entity.setCreateBy(1665943054155702273L);
        entity.setUpdateAt(new Date());
        entity.setUpdateBy(1665943054155702273L);
        BeanUtils.copyProperties(dto, entity);

        int result = this.baseMapper.insert(entity);

        return ResponseMessage.ok(result > 0);
    }

    @Override
    public ResponseMessage<RoleInfoVO> retrieveRoleInfoByRoleId(Long roleId) {
        RoleInfoVO response = new RoleInfoVO();
        LambdaQueryWrapper<RoleInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleInfoEntity::getId, roleId);

        RoleInfoEntity roleInfoEntity = this.baseMapper.selectOne(queryWrapper);

        BeanUtils.copyProperties(roleInfoEntity, response);

        return ResponseMessage.ok(response);
    }

    @Override
    public ResponseMessage<RoleInfoVO> retrieveRoleInfoByRoleName(String roleName) {
        RoleInfoVO response = new RoleInfoVO();
        LambdaQueryWrapper<RoleInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleInfoEntity::getName, roleName);

        RoleInfoEntity roleInfoEntity = this.baseMapper.selectOne(queryWrapper);

        BeanUtils.copyProperties(roleInfoEntity, response);

        return ResponseMessage.ok(response);
    }
}
