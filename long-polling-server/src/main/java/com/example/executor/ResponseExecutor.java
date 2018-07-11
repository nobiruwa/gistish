package com.example.executor;

import com.example.session.SessionContainer;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Runnable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.AsyncContext;

// TODO どこまでをsynchronizedで囲むべきか？コネクションアクセスに関するよりよい排他方式は何か？
// TODO AsyncContextにアクセスしたとき例外をどう処理すべきか？
// TODO クライアントごとに溜まったメッセージが存在する場合のリトライ戦略をどう記述すべきか？
@ApplicationScoped
public class ResponseExecutor {
    private Object synchronous;
    private SessionContainer contexts;
    private ExecutorService pool;

    public ResponseExecutor() {
    }

    public void handle(@Observes @Initialized(ApplicationScoped.class) Object event) {
        this.synchronous = new Object();
        this.contexts = new SessionContainer();

        int poolSize = 10;
        this.pool = Executors.newFixedThreadPool(poolSize);
    }

    @PreDestroy
    public void destroy() {
        this.pool.shutdownNow();
    }

    public void sendIfAvailable(String key, String message) throws IOException {
        // TODO 複数のスレッドが同一のコンテキストにアクセスするとは考えられず、
        // Executorに対し、コンテキストのcheck in/check outを行う機構が必要と思われるが、
        // SessionCloakとSessionContainerの役割が被っているのが問題か
        this.pool.execute(new MessageHandler(new SessionCloak(this.contexts), key, message));
    }

    public void add(String key, AsyncContext context) {
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
                if (sessionContainer.containsKey(this.key)) {
                    AsyncContext ctx = sessionContainer.get(this.key);
                    PrintWriter writer = ctx.getResponse().getWriter();
                    try {
                        writer.println(this.message);
                    } finally {
                        writer.close();
                        ctx.complete();
                        sessionContainer.remove(this.key);

                        // 残ったコンテキストをチェックイン
                        this.sessionCloak.checkin(sessionContainer);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
