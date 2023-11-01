package com.sven.system.service;

import java.util.List;
import java.util.Set;

import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.MenuDTO;
import com.sven.common.vo.MenuVO;

public interface IRoleMenuService {

    ResponseMessage<Boolean> createMenu(MenuDTO dto);

    ResponseMessage<Boolean> hasPerimission(Set<String> authority, String requestPath);

    ResponseMessage<List<MenuVO>> retrieveRoleMenuInfoByRoleId(Long roleId);

}
