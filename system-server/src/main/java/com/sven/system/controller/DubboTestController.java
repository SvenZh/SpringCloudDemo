package com.sven.system.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sven.common.domain.message.IBaseResponseMessage;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.dubbo.server.IPaymentService;

@RestController
@RequestMapping("/dubbo-test")
public class DubboTestController {

    @Autowired
    private IPaymentService paymentService;

    @GetMapping("/payment")
    public IBaseResponseMessage<Boolean> payment() {
        boolean response = paymentService.payment("aliPay", new BigDecimal(111));

        return ResponseMessage.ok(response);
    }

    @GetMapping("/isSupport")
    public IBaseResponseMessage<Boolean> isSupport(@RequestParam("payCode") String payCode) {
        boolean response = paymentService.isSupport("payCode");

        return ResponseMessage.ok(response);
    }
}
