package com.example.websocket;

import com.example.websocket.WebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Configuration
@ComponentScan(basePackages = {"com.example.websocket", "com.example.executor"})
public class WebSocketConfig {
    @Autowired
    Properties properties;

    @Bean
    public WebSocketConnectionManager connectionManager() {
        WebSocketConnectionManager manager = new WebSocketConnectionManager(client(), handler(), uri());
        manager.setAutoStartup(true);
        return manager;
    }

    @Bean
    public String uri() {
        String endpointURI = null;
        endpointURI = properties.getProperty("websocket.client.endpoint");
        return endpointURI;
    }

    @Bean
    public StandardWebSocketClient client() {
        return new StandardWebSocketClient();
    }

    @Bean
    public WebSocketHandler handler() {
        return new WebSocketHandler();
    }
}
