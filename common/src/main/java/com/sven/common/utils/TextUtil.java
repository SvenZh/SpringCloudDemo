package com.sven.common.utils;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.cloud.nacos.annotation.NacosConfigListener;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;

@Component
public class TextUtil {

    @Autowired 
    private ObjectMapper mapper;

    private static Map<String, TranslationItem> localization = new ConcurrentHashMap<>();

    public static String getMessage(String key, String local) {
        LangText text = localization.get(key).getMessage().stream().filter(item -> item.getLang().equals(local))
                .findFirst().orElseGet(LangText::new);
        return text.getText();
    }

    @NacosConfigListener(dataId = "localization.properties", group = "dev", initNotify = true)
    public void onTranslationChanged(String configContent) throws Exception {
        this.refreshCache(configContent);
    }

    public void refreshCache(String configContent) throws Exception {
        Properties props = new Properties();
        props.load(new StringReader(configContent));
        for (String key : props.stringPropertyNames()) {
            localization.put(key, mapper.readValue(props.getProperty(key), TranslationItem.class));
        }
    }

    @Getter 
    @Setter 
    public static class TranslationItem {
        private List<LangText> message;
    }

    @Getter 
    @Setter 
    public static class LangText {
        private String lang;
        private String text;
    }

}
