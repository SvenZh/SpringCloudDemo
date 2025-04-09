package com.sven.business.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sven.business.event.PaymentEvent;

@Service
public class WeCharPaymentServiceImpl extends PaymentAbstract {

    @Autowired
    private ApplicationContext applicationContext;
    
    @Override
    public boolean pay(BigDecimal price) {
        applicationContext.publishEvent(new PaymentEvent("WeChar pay"));
        
        return true;
    }

    @Override
    public boolean isSupport(Integer type) {
        return 1 == type;
    }

    @Override
    public int getOrder() {
        return 1;
    }

}
