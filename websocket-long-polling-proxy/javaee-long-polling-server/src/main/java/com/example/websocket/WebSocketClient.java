package com.example.websocket;

import com.example.executor.ResponseExecutor;

import java.io.IOException;
import java.net.URI;
import java.util.Properties;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.interceptor.InvocationContext;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.CloseReason;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
@ClientEndpoint
public class WebSocketClient {
    private URI endpointURI;
    private EndpointConfig config;
    private Session session;
    @Inject
    private ResponseExecutor responseExecutor;

    private static final Logger LOGGER = Logger.getLogger(WebSocketClient.class.getName());

    public WebSocketClient() {
    }

    public void handle(@Observes @Initialized(ApplicationScoped.class) Object event) {
        this.open();
    }

    public void open() {
        try {
            Properties properties = new Properties();
            properties.load(this.getClass().getResourceAsStream("/websocket-client.properties"));
            String endpoint = properties.getProperty("websocket.client.endpoint");
            URI endpointURI = new URI(endpoint);
            this.open(endpointURI);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void open(URI endpointURI) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            LOGGER.info("WebSocketClient#open(" + endpointURI.toString() + ")");
            container.connectToServer(this, endpointURI);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendIfAvailable() throws IOException {
        if (this.session != null) {
            this.session.getBasicRemote().sendText("{}");
        } else {
            LOGGER.info("session is null.");
        }
    }
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        this.config = config;
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        LOGGER.info("WebSocketClient @OnMessage onMessage(...)");
        LOGGER.info("WebSocketClient @OnMessage onMessage(...) message: " + message);
        // メッセージのパース、宛先の特定、メッセージの送信
        responseExecutor.sendIfAvailable(message);
    }

    @OnError
    public void processError(Session session, Throwable throwable) {
        LOGGER.info("WebSocketClient @OnError onError(...)");
        throwable.printStackTrace();
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        LOGGER.info("WebSocketClient @OnClose onClose(...)");
        LOGGER.info(closeReason.toString());
        this.session = null;
        this.config = null;
    }
}
