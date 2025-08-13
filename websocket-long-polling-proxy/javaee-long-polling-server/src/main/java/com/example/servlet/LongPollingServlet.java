package com.example.servlet;

import com.example.executor.ResponseExecutor;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.inject.Inject;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name="LongPollingServlet", urlPatterns={ "/subscribe" }, asyncSupported=true)
public class LongPollingServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LongPollingServlet.class.getName());

    @Inject
    private ResponseExecutor responseExecutor;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.info("LongPollingServlet#doGet() got a new connection.");
        AsyncContext ctx = request.startAsync();
        responseExecutor.add(this.getKeyStrategically(request), ctx);
    }

    private String getKeyStrategically(HttpServletRequest request) {
        // TODO もっと賢いキーの生成を
        return request.getParameter("key");
    }

}
