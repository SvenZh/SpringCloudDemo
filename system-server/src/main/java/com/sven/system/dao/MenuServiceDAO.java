package com.sven.system.dao;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sven.common.dto.MenuDTO;
import com.sven.system.entity.MenuEntity;
import com.sven.system.mapper.MenuServiceMapper;

@Component
public class MenuServiceDAO extends ServiceImpl<MenuServiceMapper, MenuEntity>{

    public void selectList(MenuDTO dto) {
    }

    public MenuEntity selectById(Long menuId) {
        return this.baseMapper.selectById(menuId);
    }

    public Boolean createMenu(MenuEntity entity) {
        return this.baseMapper.insert(entity) > 0;
    }
}
