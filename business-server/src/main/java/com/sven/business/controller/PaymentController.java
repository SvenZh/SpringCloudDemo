package com.sven.business.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sven.business.service.PayMentStrategyContext;
import com.sven.common.domain.message.IBaseResponseMessage;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.security.NoToken;

@RestController
@RequestMapping("/pay")
public class PaymentController {

    @Autowired
    private PayMentStrategyContext payMentStrategyContext;

    @NoToken
    @GetMapping("/test")
    public ResponseEntity<? extends IBaseResponseMessage<Boolean>> pay(@RequestParam String payCode) {
        return ResponseEntity.ok(ResponseMessage.ok(payMentStrategyContext.toPay(payCode, new BigDecimal("100"))));
    }
}
