package com.sven.system.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.RolePerimissionDTO;
import com.sven.common.vo.PerimissionVO;
import com.sven.common.vo.RoleVO;
import com.sven.system.dao.RolePerimissionServiceDAO;
import com.sven.system.entity.RolePerimissionEntity;
import com.sven.system.service.IPerimissionService;
import com.sven.system.service.IRolePerimissionService;
import com.sven.system.service.IRoleService;

@Service
public class RolePerimissionService implements IRolePerimissionService {

    @Autowired
    private IPerimissionService perimissionService;

    @Autowired
    private RolePerimissionServiceDAO rolePerimissionServiceDAO;

    @Autowired
    private IRoleService roleService;

    @Override
    public ResponseMessage<List<PerimissionVO>> retrieveRolePerimissionInfoByRoleId(final Long roleId) {

        RolePerimissionDTO dto = new RolePerimissionDTO();
        dto.setRoleId(roleId);

        List<RolePerimissionEntity> rolePerimissionInfoEntities = rolePerimissionServiceDAO.selectList(dto);

        List<PerimissionVO> response = rolePerimissionInfoEntities.stream().map(entity -> {
            ResponseMessage<PerimissionVO> result = perimissionService
                    .retrievePerimissionInfoById(entity.getPermissionId());
            if (result.isSuccess()) {
                return result.getData();
            }

            throw new RuntimeException("找不到权限!");
        }).collect(Collectors.toList());

        return ResponseMessage.ok(response);
    }

    @Override
    public ResponseMessage<Boolean> createRolePerimission(final RolePerimissionDTO dto) {
        dto.getPerimissionIds().stream().forEach(perimissionId -> {
            RolePerimissionEntity rolePerimissionInfoEntity = new RolePerimissionEntity();
            rolePerimissionInfoEntity.setPermissionId(perimissionId);
            rolePerimissionInfoEntity.setRoleId(dto.getRoleId());

            rolePerimissionServiceDAO.insert(rolePerimissionInfoEntity);
        });

        return ResponseMessage.ok(true);
    }

    @Override
    public ResponseMessage<Boolean> hasPerimission(final Set<String> authority, final String requestPath) {
        Boolean response = false;

        for (String roleName : authority) {
            ResponseMessage<RoleVO> remoteRoleInfoResponse = roleService.retrieveRoleInfoByRoleName(roleName);
            if (!remoteRoleInfoResponse.isSuccess()) {
                break;
            }

            ResponseMessage<List<PerimissionVO>> remotePermissionInfoResponse = this
                    .retrieveRolePerimissionInfoByRoleId(remoteRoleInfoResponse.getData().getId());

            if (!remotePermissionInfoResponse.isSuccess()) {
                break;
            }

            List<PerimissionVO> perimissionInfos = remotePermissionInfoResponse.getData();

            if (perimissionInfos.stream()
                    .anyMatch(permission -> requestPath.equalsIgnoreCase(permission.getPermission()))) {
                response = true;
                break;
            }
        }

        return ResponseMessage.ok(response);
    }
}
