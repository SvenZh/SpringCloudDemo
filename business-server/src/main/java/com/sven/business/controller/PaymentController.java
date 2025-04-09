package com.sven.business.controller;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sven.business.service.IPaymentService;
import com.sven.common.domain.message.IBaseResponseMessage;
import com.sven.common.domain.message.ResponseMessage;
import com.sven.common.security.NoToken;

@RestController
@RequestMapping("/pay")
public class PaymentController {

    @Autowired
    private List<IPaymentService> paymentService;

    @NoToken
    @GetMapping("/test")
    public ResponseEntity<? extends IBaseResponseMessage<Boolean>> pay(@RequestParam Integer type) {
        Optional<IPaymentService> optional = paymentService.stream().filter(item -> item.isSupport(type))
                .max(Comparator.comparingInt(Ordered::getOrder));

        return ResponseEntity.ok(ResponseMessage.ok(optional.get().payment(new BigDecimal("100"))));
    }
}
