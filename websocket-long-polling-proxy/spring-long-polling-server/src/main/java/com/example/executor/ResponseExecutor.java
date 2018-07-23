package com.example.executor;

import com.example.proxy.message.ProxyMessage;
import com.example.session.SessionContainer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ResponseExecutor {
    private SessionContainer contexts;
    private ExecutorService pool;
    private final ObjectMapper mapper = new ObjectMapper();
    private static final Logger LOGGER = Logger.getLogger(ResponseExecutor.class.getName());

    public ResponseExecutor() {
    }

    @PostConstruct
    public void initialize() {
        this.contexts = new SessionContainer();

        int poolSize = 10;
        this.pool = Executors.newFixedThreadPool(poolSize);
    }

    @PreDestroy
    public void destroy() {
        this.pool.shutdownNow();
    }

    public void sendIfAvailable(String message) {
        try {
            mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            ProxyMessage proxyMessage = mapper.readValue(message, ProxyMessage.class);
            this.sendIfAvailable(proxyMessage.getKey(), message);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void sendIfAvailable(String key, String message) throws IOException {
        // TODO 複数のスレッドが同一のコンテキストにアクセスするとは考えられず、
        // Executorに対し、コンテキストのcheck in/check outを行う機構が必要と思われるが、
        // SessionCloakとSessionContainerの役割が被っているのが問題か
        this.pool.execute(new MessageHandler(new SessionCloak(this.contexts), key, message));
    }

    public void add(String key, DeferredResult<ResponseEntity<String>> context) {
        this.contexts.put(key, context);
    }

    public void remove(String key) {
        this.contexts.remove(key);
    }

    private class SessionCloak {
        private SessionContainer sessionContainer;

        public SessionCloak(SessionContainer sessionContainer) {
            this.sessionContainer = sessionContainer;
        }

        public void checkin(SessionContainer sessionContainer) {
            // TODO 実装
        }

        public SessionContainer checkout() {
            // TODO 一部のコンテキストのみを含むようにする
            return this.sessionContainer;
        }
    }

    private class MessageHandler implements Runnable {
        private SessionCloak sessionCloak;
        private String key;
        private String message;

        MessageHandler(SessionCloak sessionCloak,
                       String key,
                       String message) {
            this.sessionCloak = sessionCloak;
            this.key = key;
            this.message = message;
        }

        public void run() {
            try {
                // コンテキストのチェックアウト
                SessionContainer sessionContainer = this.sessionCloak.checkout();
                LOGGER.info("ResponseExecutor.MessageHandler#run() can send?: " + (sessionContainer.containsKey(this.key) ? "yes" : "no"));
                if (sessionContainer.containsKey(this.key)) {
                    LOGGER.info("ResponseExecutor.MessageHandler#run() is trying to set a result.");
                    DeferredResult<ResponseEntity<String>> deferredResult = sessionContainer.get(this.key);
                    ResponseEntity<String> response = ResponseEntity.status(HttpStatus.OK).header("Content-Type", "application/json").body(message);
                    deferredResult.setResult(response);
                    sessionContainer.remove(this.key);

                    // 残ったコンテキストをチェックイン
                    this.sessionCloak.checkin(sessionContainer);
                }
            } catch (Exception ex) {
                LOGGER.info("ResponseExecutor.MessageHandler#run() raised an exception.");
                ex.printStackTrace();
            }
        }
    }
}
