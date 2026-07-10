package com.sven.system.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.PermissionDTO;
import com.sven.common.vo.PermissionVO;
import com.sven.system.dao.PermissionServiceDAO;
import com.sven.system.entity.PermissionEntity;
import com.sven.system.service.IPermissionService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PermissionServiceImpl implements IPermissionService {
    
    private final PermissionServiceDAO permissionServiceDAO;
    
    @Override
    public ResponseMessage<List<PermissionVO>> retrievePermissionList(final PermissionDTO dto) {
        List<PermissionEntity> permissionInfoEntities = permissionServiceDAO.selectList(dto);

        List<PermissionVO> response = permissionInfoEntities.stream().map(entity -> {
            PermissionVO vo = new PermissionVO();
            BeanUtils.copyProperties(entity, vo);

            return vo;
        }).collect(Collectors.toList());

        return ResponseMessage.ok(response);
    }

    @Override
    public ResponseMessage<IPage<PermissionVO>> retrievePermissionPage(final PermissionDTO dto) {
        IPage<PermissionEntity> permissionInfoEntities = permissionServiceDAO.paging(dto);

        IPage<PermissionVO> response = permissionInfoEntities.convert(entity -> {
            PermissionVO vo = new PermissionVO();
            BeanUtils.copyProperties(entity, vo);

            return vo;
        });

        return ResponseMessage.ok(response);
    }

    @Override
    public ResponseMessage<Boolean> createPermission(final PermissionDTO dto) {
        PermissionEntity entity = new PermissionEntity();
        BeanUtils.copyProperties(dto, entity);

        int result = permissionServiceDAO.insert(entity);
        
        return ResponseMessage.ok(result > 0);
    }

    @Override
    public ResponseMessage<PermissionVO> retrievePermissionInfoById(final Long permissionId) {
        PermissionVO response = new PermissionVO();
        PermissionEntity entity = permissionServiceDAO.selectById(permissionId);

        BeanUtils.copyProperties(entity, response);

        return ResponseMessage.ok(response);
    }
}
