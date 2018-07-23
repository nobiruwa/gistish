package com.example.websocket;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebSocketServer extends TextWebSocketHandler {
    private static final Logger LOGGER = Logger.getLogger(WebSocketServer.class.getName());
    @Autowired
    private SessionHandler sessionHandler;

    public WebSocketServer() {
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOGGER.info("WebSocketServer#afterConnectionEstablished() get a new client.");
        this.sessionHandler.addSession(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        LOGGER.info("WebSocketServer#afterConnectionClosed() lost a client.");
        this.sessionHandler.removeSession(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        LOGGER.info("WebSocketServer#handleTransportError()");
        exception.printStackTrace();
    }
}
