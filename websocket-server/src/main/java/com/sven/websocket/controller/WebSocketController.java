package com.sven.websocket.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sven.websocket.handler.TestWebSocketHandler;

@RestController
public class WebSocketController {

    @GetMapping("/sendMessage/{uid}")
    public void sendMessageById(@PathVariable("uid") Long uid, @RequestParam("message") String message) {
        TestWebSocketHandler.sendMessage(uid, message);
    }

    @GetMapping("/sendMessageToAll")
    public void groupSending(@RequestParam("message") String message) {
        TestWebSocketHandler.sendMessageToAll(message);
    }
}
