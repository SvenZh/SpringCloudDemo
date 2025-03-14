package com.sven.gateway.provider;

import java.util.ArrayList;
import java.util.List;

public class OAuthProvider {
    public static final String TARGET = "/**";
    public static final String REPLACEMENT = "";
    public static final String AUTH_KEY = "Authorization";
    private static List<String> defaultSkipUrl = new ArrayList<>();
    static {
        defaultSkipUrl.add("/test/**");
    }

    public static List<String> getDefaultSkipUrl() {
        return defaultSkipUrl;
    }
}
