package com.sven.system.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sven.common.domain.message.IBaseResponseMessage;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dubbo.server.IPaymentService;

@RestController
@RequestMapping("/dubbo-test")
public class DubboTestController {

    @Autowired
    private IPaymentService paymentService;

    @GetMapping("/list")
    public IBaseResponseMessage<Boolean> retrievePerimissionList() {
        boolean response = paymentService.payment(new BigDecimal(11));

        return ResponseMessage.ok(response);
    }
}
