package com.sven.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sven.common.domain.message.IBaseResponseMessage;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dto.PerimissionDTO;
import com.sven.common.vo.PerimissionVO;
import com.sven.system.service.IPerimissionService;

@RestController
@RequestMapping("/perimission")
public class PerimissionController {

    @Autowired
    private IPerimissionService perimissionService;
    
    @PostMapping("/list")
    public IBaseResponseMessage<List<PerimissionVO>> retrievePerimissionList(@RequestBody final PerimissionDTO dto) {
        ResponseMessage<List<PerimissionVO>> response = perimissionService.retrievePerimissionList(dto);

        return response;
    }
    
    @PostMapping("/page")
    public IBaseResponseMessage<IPage<PerimissionVO>> retrieveRolePage(@RequestBody final PerimissionDTO dto) {
        ResponseMessage<IPage<PerimissionVO>> response = perimissionService.retrievePerimissionPage(dto);
        
        return response;
    }
    
    @PostMapping("/creation")
    public IBaseResponseMessage<Boolean> createPerimission(@RequestBody @Validated final PerimissionDTO dto) {
        ResponseMessage<Boolean> response = perimissionService.createPerimission(dto);
        
        return response;
    }
}
