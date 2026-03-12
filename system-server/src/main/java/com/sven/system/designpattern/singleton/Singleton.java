package com.sven.system.designpattern.singleton;

/**
 * 单例模式
 */
public class Singleton {
    private static volatile Singleton instance = null;

    // 私有构造方法不能直接使用new创建对象
    private Singleton() {

    }

    // 双重校验方式
    public static Singleton getInstanceWithDcl() {
        // 第一层校验：为空直接返回，避免锁竞争
        if (instance == null) {
            synchronized (Singleton.class) {
                // 第二层：多个线程通过第一层校验，获取到锁之后再次判断是否为空，避免重复创建
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }

        return instance;
    }

    // 静态内部类：同一个加载器下，一个类型只会初始化一次
    public static class SingletonHolder{
        private static final Singleton INSTANCE = new Singleton();
    }

    public static Singleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
