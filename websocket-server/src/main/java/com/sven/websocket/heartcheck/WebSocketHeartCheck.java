package com.sven.websocket.heartcheck;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sven.websocket.manage.WebSocketManage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableScheduling
public class WebSocketHeartCheck {
    
    @Scheduled(cron = "0/30 * * * * ?")
    public void clearOrders() {
        int num = 0;
        try {
            num = WebSocketManage.heartCheck("HEART CHECK");
        } finally {
            log.info("websocket心跳检测结果，共【" + num + "】个连接");
        }
    }
}
