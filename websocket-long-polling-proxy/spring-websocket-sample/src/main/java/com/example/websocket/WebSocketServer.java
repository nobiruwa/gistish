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
    private static final Logger logger = Logger.getLogger(WebSocketServer.class.getName());
    @Autowired
    private SessionHandler sessionHandler;

    public WebSocketServer() {
        logger.log(Level.INFO, null, this.sessionHandler == null);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("WebSocketServer#afterConnectionEstablished() get a new client.");
        this.sessionHandler.addSession(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        this.sessionHandler.removeSession(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        this.logger.log(Level.INFO, null, this.sessionHandler == null);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        System.out.println("WebSocketServer#handleTransportError()");
        exception.printStackTrace();
    }
}
