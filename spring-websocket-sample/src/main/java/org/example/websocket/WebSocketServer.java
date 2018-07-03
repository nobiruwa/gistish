package org.example.websocket;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.logging.Level;
import java.util.logging.Logger;

@javax.enterprise.context.ApplicationScoped
@ServerEndpoint("/actions")
public class WebSocketServer {
    private final SessionHandler sessionHandler = new SessionHandler();

    public WebSocketServer() {
        Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, null, this.sessionHandler == null);
    }
    @OnOpen
    public void open(Session session) {
        this.sessionHandler.addSession(session);
    }

    @OnClose
    public void close(Session session) {
        this.sessionHandler.removeSession(session);
    }

    @OnError
    public void onError(Throwable error) {
        Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, null, error);
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();
            Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, null, this.sessionHandler == null);
            this.sessionHandler.sendToAllConnectedSessions(this.sessionHandler.createAddMessage("Thank you!"));
        }
    }
}
