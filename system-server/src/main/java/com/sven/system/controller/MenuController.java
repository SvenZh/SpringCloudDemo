package com.sven.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sven.common.domain.message.IBaseResponseMessage;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.MenuDTO;
import com.sven.common.vo.MenuVO;
import com.sven.system.service.IMenuService;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private IMenuService menuService;
    
    @PostMapping("/list")
    public IBaseResponseMessage<List<MenuVO>> retrievePerimissionList(@RequestBody final MenuDTO dto) {
        ResponseMessage<List<MenuVO>> response = menuService.retrieveMenuList(dto);

        return response;
    }
    
    @PostMapping("/creation")
    public IBaseResponseMessage<Boolean> createPerimission(@RequestBody @Validated final MenuDTO dto) {
        ResponseMessage<Boolean> response = menuService.createMenu(dto);
        
        return response;
    }
}
