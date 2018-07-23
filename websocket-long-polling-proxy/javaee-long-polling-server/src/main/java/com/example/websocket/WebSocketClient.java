package com.example.websocket;

import com.example.executor.ResponseExecutor;

import java.io.IOException;
import java.net.URI;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.interceptor.InvocationContext;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

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
