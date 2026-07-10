package com.sven.system.designpattern.decorator;

import java.math.BigDecimal;

// 糖装饰器：调用原始对象的相应方法外，还会加上自己的“装饰”逻辑
public class Sugar extends CondimentDecorator {

    public Sugar(Beverage beverage) {
        super(beverage);
    }

    @Override
    public BigDecimal cost() {
        return beverage.cost().add(new BigDecimal("2"));
    }

    @Override
    public String getDesc() {
        return beverage.getDesc() + "，加糖";
    }

}
