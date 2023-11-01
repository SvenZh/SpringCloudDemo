package com.sven.system.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.MenuDTO;
import com.sven.common.vo.MenuVO;
import com.sven.system.dao.MenuServiceDAO;
import com.sven.system.entity.MenuEntity;
import com.sven.system.service.IMenuService;

@Service
public class MenuServiceImpl implements IMenuService{

    @Autowired
    private MenuServiceDAO menuServiceDAO;
    
    @Override
    public ResponseMessage<List<MenuVO>> retrieveMenuList(MenuDTO dto) {
        
        dto.getRoleName();
        
        return null;
    }

    @Override
    public ResponseMessage<Boolean> createMenu(MenuDTO dto) {
        return null;
    }

    @Override
    public ResponseMessage<MenuVO> retrieveMenuInfoById(Long menuId) {
        
        MenuVO response = new MenuVO();
        MenuEntity entity = menuServiceDAO.selectById(menuId);

        BeanUtils.copyProperties(entity, response);
        
        return ResponseMessage.ok(response);
    }

}
