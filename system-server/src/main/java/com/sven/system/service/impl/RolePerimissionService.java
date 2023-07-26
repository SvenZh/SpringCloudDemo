package com.sven.system.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.RolePerimissionDTO;
import com.sven.common.vo.PerimissionInfoVO;
import com.sven.system.entity.RolePerimissionInfoEntity;
import com.sven.system.mapper.RolePerimissionServiceMapper;
import com.sven.system.service.IPerimissionService;
import com.sven.system.service.IRolePerimissionService;

@Service
public class RolePerimissionService extends ServiceImpl<RolePerimissionServiceMapper, RolePerimissionInfoEntity>
        implements IRolePerimissionService {

    @Autowired
    private IPerimissionService perimissionService;

    @Override
    public ResponseMessage<List<PerimissionInfoVO>> retrieveRolePerimissionInfoByRoleId(Long roleId) {

        LambdaQueryWrapper<RolePerimissionInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RolePerimissionInfoEntity::getRoleId, roleId);

        List<RolePerimissionInfoEntity> rolePerimissionInfoEntities = this.baseMapper.selectList(queryWrapper);

        List<PerimissionInfoVO> response = rolePerimissionInfoEntities.stream().map(entity -> {
            ResponseMessage<PerimissionInfoVO> result = perimissionService
                    .retrievePerimissionInfoById(entity.getPermissionId());
            if (result.isSuccess()) {
                return result.getData();
            }

            throw new RuntimeException("找不到权限!");
        }).collect(Collectors.toList());

        return ResponseMessage.ok(response);
    }

    @Override
    public ResponseMessage<Boolean> createRolePerimission(RolePerimissionDTO dto) {
        dto.getRoleIds().parallelStream().forEach(roleId -> {
            dto.getPerimissionIds().stream().forEach(perimissionId -> {
                RolePerimissionInfoEntity rolePerimissionInfoEntity = new RolePerimissionInfoEntity();
                rolePerimissionInfoEntity.setPermissionId(perimissionId);
                rolePerimissionInfoEntity.setRoleId(roleId);

                this.baseMapper.insert(rolePerimissionInfoEntity);
            });
        });

        return ResponseMessage.ok(true);
    }
}
