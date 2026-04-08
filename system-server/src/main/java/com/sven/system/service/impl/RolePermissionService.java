package com.sven.system.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.RolePermissionDTO;
import com.sven.common.vo.PermissionVO;
import com.sven.common.vo.RoleVO;
import com.sven.system.dao.RolePermissionServiceDAO;
import com.sven.system.entity.RolePermissionEntity;
import com.sven.system.service.IPermissionService;
import com.sven.system.service.IRolePermissionService;
import com.sven.system.service.IRoleService;

@Service
public class RolePermissionService implements IRolePermissionService {

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private RolePermissionServiceDAO rolePermissionServiceDAO;

    @Autowired
    private IRoleService roleService;

    @Override
    public ResponseMessage<List<PermissionVO>> retrieveRolePermissionInfoByRoleId(final Long roleId) {

        RolePermissionDTO dto = new RolePermissionDTO();
        dto.setRoleId(roleId);

        List<RolePermissionEntity> rolePermissionInfoEntities = rolePermissionServiceDAO.selectList(dto);

        List<PermissionVO> response = rolePermissionInfoEntities.stream().map(entity -> {
            ResponseMessage<PermissionVO> result = permissionService
                    .retrievePermissionInfoById(entity.getPermissionId());
            if (result.isSuccess()) {
                return result.getData();
            }

            throw new RuntimeException("找不到权限!");
        }).collect(Collectors.toList());

        return ResponseMessage.ok(response);
    }

    @Override
    public ResponseMessage<Boolean> createRolePermission(final RolePermissionDTO dto) {
        dto.getPermissionIds().stream().forEach(permissionId -> {
            RolePermissionEntity rolePermissionInfoEntity = new RolePermissionEntity();
            rolePermissionInfoEntity.setPermissionId(permissionId);
            rolePermissionInfoEntity.setRoleId(dto.getRoleId());

            rolePermissionServiceDAO.insert(rolePermissionInfoEntity);
        });

        return ResponseMessage.ok(true);
    }

    @Override
    public ResponseMessage<Boolean> hasPermission(final Set<String> authority, final String requestPath) {
        Boolean response = false;

        for (String roleName : authority) {
            ResponseMessage<RoleVO> remoteRoleInfoResponse = roleService.retrieveRoleInfoByRoleName(roleName);
            if (!remoteRoleInfoResponse.isSuccess()) {
                break;
            }

            ResponseMessage<List<PermissionVO>> remotePermissionInfoResponse = this
                    .retrieveRolePermissionInfoByRoleId(remoteRoleInfoResponse.getData().getId());

            if (!remotePermissionInfoResponse.isSuccess()) {
                break;
            }

            List<PermissionVO> permissionInfos = remotePermissionInfoResponse.getData();

            if (permissionInfos.stream()
                    .anyMatch(permission -> requestPath.equalsIgnoreCase(permission.getPermission()))) {
                response = true;
                break;
            }
        }

        return ResponseMessage.ok(response);
    }
}
