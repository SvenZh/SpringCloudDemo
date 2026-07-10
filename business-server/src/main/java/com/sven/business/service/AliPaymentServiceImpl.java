package com.sven.business.service;

import java.math.BigDecimal;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sven.business.event.PaymentEvent;
import com.sven.common.annotation.PayStrategyAnnotation;

import lombok.AllArgsConstructor;

@Service("aliPay")
@PayStrategyAnnotation("aliPay")
@AllArgsConstructor
public class AliPaymentServiceImpl extends PaymentAbstract {

    private final ApplicationContext applicationContext;

    @Override
    public boolean pay(BigDecimal price) {
        applicationContext.publishEvent(new PaymentEvent("Ali pay"));
        return true;
    }

    @Override
    public boolean isSupport(String payCode) {
        return "aliPay".equals(payCode);
    }

    @Override
    public int getOrder() {
        return 2;
    }

}
