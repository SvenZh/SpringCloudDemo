package com.sven.system.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.MenuDTO;
import com.sven.common.vo.MenuVO;
import com.sven.system.dao.RoleMenuServiceDAO;
import com.sven.system.entity.RoleMenuEntity;
import com.sven.system.service.IMenuService;
import com.sven.system.service.IRoleMenuService;

@Service
public class RoleMenuServiceImpl implements IRoleMenuService{

    @Autowired
    private RoleMenuServiceDAO roleMenuServiceDAO;
    
    @Autowired
    private IMenuService menuService;
    
    @Override
    public ResponseMessage<List<MenuVO>> retrieveRoleMenuInfoByRoleId(Long roleId) {
        
        List<RoleMenuEntity> RoleMenuEntities = roleMenuServiceDAO.retrieveRolePerimissionInfoByRoleId(roleId);
        
        List<MenuVO> response = RoleMenuEntities.stream().map(entity -> {
            ResponseMessage<MenuVO> result = menuService
                    .retrieveMenuInfoById(entity.getMenuId());
            if (result.isSuccess()) {
                return result.getData();
            }

            throw new RuntimeException("找不到权限!");
        }).collect(Collectors.toList());

        return ResponseMessage.ok(response);
    }
    
    @Override
    public ResponseMessage<Boolean> createMenu(MenuDTO dto) {
        return null;
    }

    @Override
    public ResponseMessage<Boolean> hasPerimission(Set<String> authority, String requestPath) {
        return null;
    }

}
