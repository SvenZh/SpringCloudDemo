package com.sven.business.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sven.business.event.PaymentEvent;

@Service
public class AliPaymentServiceImpl extends PaymentAbstract {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public boolean pay(BigDecimal price) {
        applicationContext.publishEvent(new PaymentEvent("Ali pay"));
        return true;
    }

    @Override
    public boolean isSupport(Integer type) {
        return 2 == type;
    }

    @Override
    public int getOrder() {
        return 2;
    }

}
