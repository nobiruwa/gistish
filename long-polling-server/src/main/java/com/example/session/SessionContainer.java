package com.example.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.servlet.AsyncContext;

public class SessionContainer extends ConcurrentHashMap<String, AsyncContext> {
    public SessionContainer() {
    }
}
