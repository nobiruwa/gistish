package com.example.websocket;

import com.example.executor.ResponseExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WebSocketHandler extends TextWebSocketHandler {
    @Autowired
    private ResponseExecutor responseExecutor;
    private static final Logger LOGGER = Logger.getLogger(WebSocketHandler.class.getName());

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        LOGGER.info("WebSocketHandler#handleTextMessage() got a message.");
        responseExecutor.sendIfAvailable(message.getPayload());
    }

}
