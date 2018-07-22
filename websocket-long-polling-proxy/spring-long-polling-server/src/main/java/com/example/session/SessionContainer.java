package com.example.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

@Component
public class SessionContainer extends ConcurrentHashMap<String, DeferredResult<ResponseEntity<String>>> {
    public SessionContainer() {
    }
}
