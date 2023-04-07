package com.sven.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sven.common.domain.message.IBaseResponseMessage;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.service.dto.DemoDTO;
import com.sven.service.service.IDemoService;
import com.sven.service.vo.UserInfoVO;

@RestController
public class DemoController {
    @Autowired
    private IDemoService demoService;

    @PostMapping("/demo")
    public IBaseResponseMessage<?> demo(@RequestBody DemoDTO body) {
        IPage<UserInfoVO> result = demoService.getUserInfoList(body);
        ResponseMessage<IPage<UserInfoVO>> response = new ResponseMessage<>(result, 200);

        return response;
    }
}
