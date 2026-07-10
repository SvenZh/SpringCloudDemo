package com.sven.system.designpattern.strategy;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import org.springframework.core.Ordered;

public class PayMentStrategyContext {
    private List<IPaymentService> instances;

    public boolean toPay(BigDecimal amount, String payCode) {
        IPaymentService instance = instances.stream()
                .filter(item -> item.isSupport(payCode))
                .max(Comparator.comparingInt(Ordered::getOrder))
                .orElseThrow(() -> new RuntimeException("支付类型不支持"));
        
        return instance.payment(amount);
    }
}
