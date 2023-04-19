package com.sven.gateway.provider;

import java.util.ArrayList;
import java.util.List;

public class AuthProvider {

    public static String TARGET = "/**";
    public static String REPLACEMENT = "";
    public static String AUTH_KEY = "Authorization";
    private static List<String> defaultSkipUrl = new ArrayList<>();

    static {
        defaultSkipUrl.add("/oauth/login");
        defaultSkipUrl.add("/oauth/logout");
        defaultSkipUrl.add("/oauth/token/**");
    }

    /**
     * 默认无需鉴权的API
     */
    public static List<String> getDefaultSkipUrl() {
        return defaultSkipUrl;
    }

}
