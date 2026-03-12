package com.sven.system.designpattern.strategy;

import java.math.BigDecimal;

import org.springframework.core.Ordered;

public interface IPaymentService extends Ordered {
    public boolean isSupport(String payCode);

    public boolean payment(BigDecimal amount);
}
