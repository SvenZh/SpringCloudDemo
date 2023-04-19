package com.sven.gateway.provider;

import java.util.HashMap;
import java.util.Map;

public class ResponseProvider {

    public static Map<String, Object> success(String message) {
        return response(200, message);
    }

    public static Map<String, Object> fail(String message) {
        return response(400, message);
    }
    
    public static Map<String, Object> unAuth(Integer code, String message) {
        return response(code, message);
    }
    
    public static Map<String, Object> forbidden(String message) {
        return response(403, message);
    }

    public static Map<String, Object> error(String message) {
        return response(500, message);
    }

    public static Map<String, Object> response(int status, String message) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("code", status);
        map.put("message", message);
        map.put("data", null);
        return map;
    }

}
