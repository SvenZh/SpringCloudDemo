package com.sven.system.designpattern.decorator;

import java.math.BigDecimal;

// 原始对象和装饰器对象所共有的接口
public interface Beverage {
    public String getDesc();

    public BigDecimal cost();
}
