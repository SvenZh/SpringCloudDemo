package com.sven.system.designpattern.decorator;

// 装饰器抽象类：将装饰逻辑与基础组件分离开
public abstract class CondimentDecorator implements Beverage {

    // 持有一个要装饰对象的引用
    protected Beverage beverage;

    public CondimentDecorator(Beverage beverage) {
        this.beverage = beverage;
    }

    @Override
    public abstract String getDesc();

}
