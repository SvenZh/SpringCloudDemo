package com.sven.business.service;

import java.math.BigDecimal;

import com.sven.business.service.api.IPaymentService;

public abstract class PaymentAbstract implements IPaymentService {

    @Override
    public boolean payment(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) > 0) {
            return pay(price);
        }

        return false;
    }

    protected abstract boolean pay(BigDecimal price);
}
