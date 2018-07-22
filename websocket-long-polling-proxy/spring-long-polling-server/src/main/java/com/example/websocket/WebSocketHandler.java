package com.example.websocket;

import com.example.executor.ResponseExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebSocketHandler extends TextWebSocketHandler {
    @Autowired
    private ResponseExecutor responseExecutor;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("WebSocketHandler#handleTextMessage() got a message.");
        responseExecutor.sendIfAvailable(message.getPayload());
    }

}
