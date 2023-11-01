package com.sven.system.service;

import java.util.List;

import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.MenuDTO;
import com.sven.common.vo.MenuVO;

public interface IMenuService {

    ResponseMessage<List<MenuVO>> retrieveMenuList(MenuDTO dto);

    ResponseMessage<Boolean> createMenu(MenuDTO dto);

    ResponseMessage<MenuVO> retrieveMenuInfoById(Long menuId);

}
