package com.sven.system.designpattern.decorator;

import java.math.BigDecimal;

// 被装饰的原始对象
public class Espresso implements Beverage {

    @Override
    public String getDesc() {
        return "浓缩咖啡";
    }

    @Override
    public BigDecimal cost() {
        return new BigDecimal("20");
    }
}
