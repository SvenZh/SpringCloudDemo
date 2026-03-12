package com.sven.system.designpattern.strategy;

import java.math.BigDecimal;

public class WeCharPaymentServiceImpl extends PaymentAbstract {
    @Override
    public boolean isSupport(String payCode) {
        return "weCharPay".equals(payCode);
    }

    @Override
    protected boolean pay(BigDecimal price) {
        System.out.println("微信：支付成功");
        return true;
    }

    @Override
    public int getOrder() {
        return 2;
    }

}
