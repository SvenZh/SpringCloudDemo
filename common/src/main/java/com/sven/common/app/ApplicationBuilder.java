package com.sven.common.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.util.StringUtils;

public class ApplicationBuilder {
    public static ConfigurableApplicationContext run(Class<?> source, String... args) {
        SpringApplicationBuilder builder = createSpringApplicationBuilder(source, args);
        return builder.run(args);
    }
    
    public static SpringApplicationBuilder createSpringApplicationBuilder(Class<?> source,
            String... args) {
        ConfigurableEnvironment environment = new StandardEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addFirst(new SimpleCommandLinePropertySource(args));
        propertySources.addLast(new MapPropertySource(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, environment.getSystemProperties()));
        propertySources.addLast(new SystemEnvironmentPropertySource(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, environment.getSystemEnvironment()));
        String[] activeProfiles = environment.getActiveProfiles();
        List<String> profiles = Arrays.asList(activeProfiles);
        List<String> presetProfiles = new ArrayList<>(Arrays.asList(AppConstant.DEV_CODE, AppConstant.TEST_CODE, AppConstant.PROD_CODE));
        presetProfiles.retainAll(profiles);
        List<String> activeProfileList = new ArrayList<>(profiles);
        Function<Object[], String> joinFun = StringUtils::arrayToCommaDelimitedString;
        SpringApplicationBuilder builder = new SpringApplicationBuilder(source);
        String profile;
        if (activeProfileList.isEmpty()) {
            // 默认dev开发
            profile = AppConstant.DEV_CODE;
            activeProfileList.add(profile);
            builder.profiles(profile);
        } else if (activeProfileList.size() == 1) {
            profile = activeProfileList.get(0);
        } else {
            // 同时存在dev、test、prod环境时
            throw new RuntimeException("同时存在环境变量:[" + StringUtils.arrayToCommaDelimitedString(activeProfiles) + "]");
        }
        
        String startJarPath = ApplicationBuilder.class.getResource("/").getPath().split("!")[0];
        String activePros = joinFun.apply(activeProfileList.toArray());
        System.out.println(String.format("----启动中，读取到的环境变量:[%s]，jar地址:[%s]----", activePros, startJarPath));
        
        Properties props = System.getProperties();
        props.setProperty("spring.cloud.nacos.config.group", profile);
        props.setProperty("spring.cloud.nacos.config.ext-config[0].data-id", "common");
        props.setProperty("spring.cloud.nacos.config.ext-config[0].refresh", "true");
        props.setProperty("spring.cloud.nacos.config.ext-config[0].group", profile);

        props.setProperty("spring.cloud.sentinel.eager", "true");
        props.setProperty("spring.cloud.sentinel.transport.dashboard", "localhost:8858");
        props.setProperty("spring.cloud.sentinel.datasource.ds1.nacos.namespace", "SpringCloudDemo");
        props.setProperty("spring.cloud.sentinel.datasource.ds1.nacos.server-addr", "localhost:8848");
        props.setProperty("spring.cloud.sentinel.datasource.ds1.nacos.dataId", "sentinel-gateway-flow");
        props.setProperty("spring.cloud.sentinel.datasource.ds1.nacos.groupId", profile);
        props.setProperty("spring.cloud.sentinel.datasource.ds1.nacos.data-type", "json");
        props.setProperty("spring.cloud.sentinel.datasource.ds1.nacos.rule-type", "gw-flow");
        
        return builder;
    }

}
