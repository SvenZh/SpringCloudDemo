package com.sven.common.dubbo.server;

import java.math.BigDecimal;

import org.springframework.core.Ordered;

public interface IPaymentService extends Ordered {
    
    public boolean payment(BigDecimal price);

    public boolean isSupport(String payCode);
}