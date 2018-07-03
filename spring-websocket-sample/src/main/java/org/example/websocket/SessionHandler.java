package org.example.websocket;

import java.io.IOException;
import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import java.util.HashSet;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.websocket.Session;

import java.util.logging.Level;
import java.util.logging.Logger;

@javax.enterprise.context.ApplicationScoped
public class SessionHandler {
    private final Set<Session> sessions = new HashSet<>();

    public SessionHandler() {
    }

    public void addSession(Session session) {
        sessions.add(session);
    }

    public void removeSession(Session session) {
        sessions.add(session);
    }

    public JsonObject createAddMessage(String message) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject addMessage = provider.createObjectBuilder()
            .add("action", "response")
            .add("message", message)
            .build();
        return addMessage;
    }

    public void sendToAllConnectedSessions(JsonObject message) {
        for (Session session : this.sessions) {
            this.sendToSession(session, message);
        }
    }

    public void sendToSession(Session session, JsonObject message) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException ex) {
            sessions.remove(session);
            Logger.getLogger(SessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
