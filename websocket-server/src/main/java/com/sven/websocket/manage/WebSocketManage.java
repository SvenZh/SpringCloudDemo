package com.sven.websocket.manage;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketManage {
    private static Map<Long, WebSocketSessionInfo> SOCKET_POOL = new ConcurrentHashMap<>();

    public static Map<Long, WebSocketSessionInfo> getAll() {
        return SOCKET_POOL;
    }

    public static boolean isOnline(Long uid) {
        if (Objects.nonNull(SOCKET_POOL) && SOCKET_POOL.containsKey(uid)) {
            return true;
        } else {
            return false;
        }
    }

    public static int getOnlineClients() {
        if (Objects.isNull(SOCKET_POOL)) {
            return 0;
        } else {
            return SOCKET_POOL.size();
        }
    }

    public static WebSocketSessionInfo get(Long uid) {
        return SOCKET_POOL.get(uid);
    }

    public static void add(Long uid, WebSocketSessionInfo sessionInfo) {
        SOCKET_POOL.put(uid, sessionInfo);
    }

    public static WebSocketSessionInfo remove(Long uid) {
        if (SOCKET_POOL.containsKey(uid)) {
            return SOCKET_POOL.remove(uid);
        }

        return null;
    }

    public static void removeAndClose(Long uid) throws IOException {
        WebSocketSessionInfo sessionInfo = SOCKET_POOL.remove(uid);

        if (sessionInfo != null) {
            sessionInfo.getSession().close();
        }
    }

    public static synchronized int heartCheck(String message) {
        if (SOCKET_POOL.size() == 0) {
            return 0;
        }
        
        StringBuffer uids = new StringBuffer();
        AtomicInteger count = new AtomicInteger();
        SOCKET_POOL.forEach((uid, server) -> {
            count.getAndIncrement();
            if (SOCKET_POOL.containsKey(uid)) {
                WebSocketSession session = server.getSession();
                try {
                    session.sendMessage(new TextMessage(message));
                    if (count.get() == SOCKET_POOL.size() - 1) {
                        uids.append("uid");
                        return; // 跳出本次循环
                    }
                    uids.append(uid).append(",");
                } catch (IOException e) {
                    log.info("客户端检测心跳异常, 准备移除: " + uid);
                    try {
                        removeAndClose(uid);
                    } catch (IOException ignore) {
                        log.info("客户端移除异常, 移除失败: " + uid);
                    }
                    log.info("客户端检测心跳异常, 已移除: " + uid);
                }
            } else {
                log.info("客户端心跳检测异常不存在: " + uid + "，不存在！");
            }
        });
        log.info("客户端心跳检测结果: " + uids + "连接正在运行");
        return SOCKET_POOL.size();
    }
}
