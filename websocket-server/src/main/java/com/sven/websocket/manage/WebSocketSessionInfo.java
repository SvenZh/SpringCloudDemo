package com.sven.websocket.manage;

import org.springframework.web.socket.WebSocketSession;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebSocketSessionInfo {
    private WebSocketSession session;
}
