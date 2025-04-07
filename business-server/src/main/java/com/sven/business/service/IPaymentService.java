package com.sven.business.service;

import java.math.BigDecimal;

import org.springframework.core.Ordered;

public interface IPaymentService extends Ordered {
    
    public boolean payment(BigDecimal price);

    public boolean isSupport(Integer type);
}
