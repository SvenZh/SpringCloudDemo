package com.sven.websocket.handler;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.sven.websocket.manage.WebSocketManage;
import com.sven.websocket.manage.WebSocketSessionInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestWebSocketHandler implements WebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        WebSocketSessionInfo sessionInfo = new WebSocketSessionInfo();
        sessionInfo.setSession(session);
        
        Long uid = Long.valueOf(session.getAttributes().get("uid").toString());
        WebSocketManage.add(uid, sessionInfo);
        session.sendMessage(new TextMessage("连接成功"));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        Long uid = Long.valueOf(session.getAttributes().get("uid").toString());
        log.info("websocket收到客户端编号uid消息: " + uid + ", 报文: " + message.toString());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        Long uid = Long.valueOf(session.getAttributes().get("uid").toString());
        log.error("websocket编号uid错误: " + uid + "原因: " + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        Long uid = Long.valueOf(session.getAttributes().get("uid").toString());
        WebSocketManage.removeAndClose(uid);
        log.info("websocket退出编号uid: " + uid + "，当前在线数为: " + WebSocketManage.getOnlineClients());
    }

    @Override
    public boolean supportsPartialMessages() {
        return true;
    }
    
    public static boolean sendMessage(@NotNull Long uid, String message) {
        WebSocketSessionInfo sessionInfo = WebSocketManage.get(uid);

        if (Objects.nonNull(sessionInfo)) {
            WebSocketSession session = sessionInfo.getSession();
            try {
                session.sendMessage(new TextMessage(message));
                log.info("websocket发送消息编号uid为: " + uid + "发送消息: " + message);
                return true;
            } catch (Exception ex) {
                log.error("websocket发送消息失败编号uid为: " + uid + "消息: " + message);
                return false;
            }
        } else {
            log.error("websocket未连接编号uid号为: " + uid + "消息: " + message);
            return false;
        }
    }
    
    public static void sendMessageToAll(String message) {
        Map<Long, WebSocketSessionInfo> allSessionInfo = WebSocketManage.getAll();

        allSessionInfo.forEach((uid, server) -> {
            WebSocketSession session = server.getSession();
            try {
                session.sendMessage(new TextMessage(message));
                log.info("websocket群发消息编号uid为: " + uid + "，消息: " + message);
            } catch (IOException ex) {
                log.error("群发自定义消息失败: " + uid + "，message: " + message);
            }
        });
    }
    
    public static synchronized int heartCheck() {
        return WebSocketManage.heartCheck("HEART CHECK");
    }
}
