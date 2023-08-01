package com.sven.system.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.RolePerimissionDTO;
import com.sven.common.vo.PerimissionInfoVO;
import com.sven.common.vo.RoleInfoVO;
import com.sven.system.entity.RolePerimissionInfoEntity;
import com.sven.system.mapper.RolePerimissionServiceMapper;
import com.sven.system.service.IPerimissionService;
import com.sven.system.service.IRolePerimissionService;
import com.sven.system.service.IRoleService;

@Service
public class RolePerimissionService extends ServiceImpl<RolePerimissionServiceMapper, RolePerimissionInfoEntity>
        implements IRolePerimissionService {

    @Autowired
    private IPerimissionService perimissionService;

    @Autowired
    private IRoleService roleService;

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
        dto.getPerimissionIds().stream().forEach(perimissionId -> {
            RolePerimissionInfoEntity rolePerimissionInfoEntity = new RolePerimissionInfoEntity();
            rolePerimissionInfoEntity.setPermissionId(perimissionId);
            rolePerimissionInfoEntity.setRoleId(dto.getRoleId());

            this.baseMapper.insert(rolePerimissionInfoEntity);
        });

        return ResponseMessage.ok(true);
    }

    @Override
    public ResponseMessage<Boolean> hasPerimission(Set<String> authority, String requestPath) {
        Boolean response = false;

        for (String roleName : authority) {
            ResponseMessage<RoleInfoVO> remoteRoleInfoResponse = roleService.retrieveRoleInfoByRoleName(roleName);
            if (!remoteRoleInfoResponse.isSuccess()) {
                break;
            }

            ResponseMessage<List<PerimissionInfoVO>> remotePermissionInfoResponse = this
                    .retrieveRolePerimissionInfoByRoleId(remoteRoleInfoResponse.getData().getId());

            if (!remotePermissionInfoResponse.isSuccess()) {
                break;
            }

            List<PerimissionInfoVO> perimissionInfos = remotePermissionInfoResponse.getData();

            if (perimissionInfos.stream()
                    .anyMatch(permission -> requestPath.equalsIgnoreCase(permission.getPermission()))) {
                response = true;
                break;
            }
        }

        return ResponseMessage.ok(response);
    }
}
