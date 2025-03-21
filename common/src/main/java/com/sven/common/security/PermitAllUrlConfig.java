package com.sven.common.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import cn.hutool.core.util.ReUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "security.oauth2.ignore")
public class PermitAllUrlConfig implements InitializingBean {
    private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");

    private static final String[] DEFAULT_IGNORE_URLS = new String[] { "/actuator/**", "/error", "/v3/api-docs" };

    @Getter
    @Setter
    private List<String> urls = new ArrayList<>();
    
    @Override
    public void afterPropertiesSet() {
        urls.addAll(Arrays.asList(DEFAULT_IGNORE_URLS));
        RequestMappingHandlerMapping mapping = SpringUtil.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        map.keySet().forEach(info -> {
            HandlerMethod handlerMethod = map.get(info);

            // 获取方法上边的注解 替代path variable 为 *
            NoToken method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), NoToken.class);
            Optional.ofNullable(method)
                .ifPresent(inner -> Objects.requireNonNull(info.getPathPatternsCondition())
                    .getPatternValues()
                    .forEach(url -> urls.add(ReUtil.replaceAll(url, PATTERN, "*"))));

            // 获取类上边的注解, 替代path variable 为 *
            NoToken controller = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), NoToken.class);
            Optional.ofNullable(controller)
                .ifPresent(inner -> Objects.requireNonNull(info.getPathPatternsCondition())
                    .getPatternValues()
                    .forEach(url -> urls.add(ReUtil.replaceAll(url, PATTERN, "*"))));
        });
    }
}
