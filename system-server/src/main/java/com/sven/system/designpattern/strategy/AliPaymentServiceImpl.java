package com.sven.system.designpattern.strategy;

import java.math.BigDecimal;

public class AliPaymentServiceImpl extends PaymentAbstract {

    @Override
    public boolean isSupport(String payCode) {
        return "aliPay".equals(payCode);
    }

    @Override
    protected boolean pay(BigDecimal price) {
        System.out.println("支付宝：支付成功");
        return true;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
