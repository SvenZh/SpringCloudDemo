package com.sven.system.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.PermissionDTO;
import com.sven.common.vo.PermissionVO;

public interface IPermissionService {

    ResponseMessage<List<PermissionVO>> retrievePermissionList(final PermissionDTO dto);

    ResponseMessage<IPage<PermissionVO>> retrievePermissionPage(final PermissionDTO dto);

    ResponseMessage<Boolean> createPermission(final PermissionDTO dto);
    
    ResponseMessage<PermissionVO> retrievePermissionInfoById(final Long permissionId);

}
