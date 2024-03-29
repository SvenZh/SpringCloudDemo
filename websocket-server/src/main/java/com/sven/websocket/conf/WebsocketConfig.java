package com.sven.websocket.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.sven.websocket.handler.TestWebSocketHandler;
import com.sven.websocket.interceptor.WebSocketInterceptor;

@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

    @Autowired
    private TestWebSocketHandler testWebSocketHandler;
    
    @Autowired
    private WebSocketInterceptor webSocketInterceptor;
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(testWebSocketHandler, "/test/websocket")
                .addInterceptors(webSocketInterceptor)
                .setAllowedOrigins("*");
    }

}
