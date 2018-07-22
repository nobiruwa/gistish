package com.example.websocket;

import com.example.proxy.message.Message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;

@Component
public class SessionHandler {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Set<WebSocketSession> sessions = new HashSet<>();
    private static final Logger logger = Logger.getLogger(SessionHandler.class.getName());

    public SessionHandler() {
    }

    public void addSession(WebSocketSession session) {
        sessions.add(session);
    }

    public void removeSession(WebSocketSession session) {
        sessions.remove(session);
    }

    public void sendToAllConnectedSessions(Message message) {
        System.out.println("SessionHandler#sendToAllConnectedSessions() is trying to send a message to " + this.sessions.size() + " clients.");
        for (WebSocketSession session : this.sessions) {
            this.sendToSession(session, message);
        }
    }

    public void sendToSession(WebSocketSession session, Message message) {
        try {
            mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            String messageRepresentation = mapper.writeValueAsString(message);

            session.sendMessage(new TextMessage(messageRepresentation));
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            sessions.remove(session);
            ex.printStackTrace();
        }
    }
}
