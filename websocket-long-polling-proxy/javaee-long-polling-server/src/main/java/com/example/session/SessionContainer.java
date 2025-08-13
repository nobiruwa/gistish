package com.example.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.servlet.AsyncContext;

public class SessionContainer extends ConcurrentHashMap<String, AsyncContext> {
    public SessionContainer() {
    }
}
