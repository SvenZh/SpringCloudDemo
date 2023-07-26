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
import com.sven.common.dto.PerimissionInfoDTO;
import com.sven.common.vo.PerimissionInfoVO;
import com.sven.system.service.IPerimissionService;

@RestController
@RequestMapping("/perimission")
public class PerimissionController {

    @Autowired
    private IPerimissionService perimissionService;
    
    @PostMapping("/list")
    public IBaseResponseMessage<List<PerimissionInfoVO>> retrievePerimissionList(@RequestBody PerimissionInfoDTO dto) {
        ResponseMessage<List<PerimissionInfoVO>> response = perimissionService.retrievePerimissionList(dto);

        return response;
    }
    
    @PostMapping("/page")
    public IBaseResponseMessage<IPage<PerimissionInfoVO>> retrieveRolePage(@RequestBody PerimissionInfoDTO dto) {
        ResponseMessage<IPage<PerimissionInfoVO>> response = perimissionService.retrievePerimissionPage(dto);
        
        return response;
    }
    
    @PostMapping("/creation")
    public IBaseResponseMessage<Boolean> createPerimission(@RequestBody @Validated PerimissionInfoDTO dto) {
        ResponseMessage<Boolean> response = perimissionService.createPerimission(dto);
        
        return response;
    }
}
