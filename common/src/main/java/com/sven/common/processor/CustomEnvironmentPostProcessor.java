package com.sven.common.processor;

import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;

/**
 *  环境准备阶段
 *  作用:修改配置
 */
public class CustomEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
//        System.out.println("------------" + environment.getProperty("spring.application.name"));
//        Properties props = new Properties();
//        props.put("spring.application.name", environment.getProperty("spring.application.name") + "-modified");
//        PropertySource<?> propertySource = new PropertiesPropertySource("customProps", props);
//        environment.getPropertySources().addFirst(propertySource);
//        System.out.println("------------" + environment.getProperty("spring.application.name"));
    }
}
