package com.sven.system.designpattern.decorator;

public class DecoratorPatternDemo {
    public static void main(String[] args) {
        // 点一份基础的浓缩咖啡
        Beverage beverage = new Espresso();
        System.out.println(beverage.getDesc() + " ￥" + beverage.cost());

        // 给浓缩咖啡加牛奶（用牛奶装饰它）
        Beverage beverageWithMilk = new Milk(new Espresso());
        System.out.println(beverageWithMilk.getDesc() + " ￥" + beverageWithMilk.cost());

        // 给浓缩咖啡加牛奶，再加糖（多层装饰）
        Beverage beverageWithMilkAndSugar = new Sugar(new Milk(new Espresso()));
        System.out.println(beverageWithMilkAndSugar.getDesc() + " ￥" + beverageWithMilkAndSugar.cost());
    }
}
