package com.sven.system.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.RoleDTO;
import com.sven.common.vo.RoleVO;
import com.sven.system.dao.RoleServiceDAO;
import com.sven.system.entity.RoleEntity;
import com.sven.system.service.IRoleService;

@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private RoleServiceDAO roleServiceDAO;
    
    @Override
    public ResponseMessage<List<RoleVO>> retrieveRoleList(final RoleDTO dto) {
        List<RoleEntity> roleInfoEntities = roleServiceDAO.selectList(dto);

        List<RoleVO> response = roleInfoEntities.stream().map(entity -> {
            RoleVO vo = new RoleVO();
            BeanUtils.copyProperties(entity, vo);

            return vo;
        }).collect(Collectors.toList());

        return ResponseMessage.ok(response);
    }

    @Override
    public ResponseMessage<IPage<RoleVO>> rolePage(final RoleDTO dto) {

        List<RoleEntity> roleInfoEntities = roleServiceDAO.selectList(dto);

        List<RoleVO> response = roleInfoEntities.stream().map(entity -> {
            RoleVO vo = new RoleVO();
            BeanUtils.copyProperties(entity, vo);

            return vo;
        }).collect(Collectors.toList());

        Page<RoleVO> page = Page.of(dto.getPageNo(), dto.getPageSize(), response.size());
        page.setRecords(response);

        return ResponseMessage.ok(page);
    }

    @Override
    public ResponseMessage<Boolean> createRole(final RoleDTO dto) {
        RoleEntity entity = new RoleEntity();
        entity.setCreateAt(new Date());
        entity.setCreateBy(1665943054155702273L);
        entity.setUpdateAt(new Date());
        entity.setUpdateBy(1665943054155702273L);
        BeanUtils.copyProperties(dto, entity);

        int result = roleServiceDAO.insert(entity);

        return ResponseMessage.ok(result > 0);
    }

    @Override
    public ResponseMessage<RoleVO> retrieveRoleInfoByRoleId(final Long roleId) {
        RoleVO response = new RoleVO();
        
        RoleDTO dto = new RoleDTO();
        dto.setId(roleId);
        
        RoleEntity roleInfoEntity = roleServiceDAO.selectOne(dto);
        BeanUtils.copyProperties(roleInfoEntity, response);

        return ResponseMessage.ok(response);
    }

    @Override
    public ResponseMessage<RoleVO> retrieveRoleInfoByRoleName(final String roleName) {
        RoleVO response = new RoleVO();
        
        RoleDTO dto = new RoleDTO();
        dto.setName(roleName);
        RoleEntity roleInfoEntity = roleServiceDAO.selectOne(dto);

        BeanUtils.copyProperties(roleInfoEntity, response);

        return ResponseMessage.ok(response);
    }
}
