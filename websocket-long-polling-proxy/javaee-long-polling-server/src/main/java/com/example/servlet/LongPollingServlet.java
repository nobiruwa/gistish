package com.example.servlet;

import com.example.executor.ResponseExecutor;

import java.io.IOException;
import java.io.PrintWriter;
import javax.inject.Inject;
import javax.servlet.AsyncContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@WebServlet(name="LongPollingServlet", urlPatterns={ "/subscribe" }, asyncSupported=true)
public class LongPollingServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(LongPollingServlet.class);

    @Inject
    private ResponseExecutor responseExecutor;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AsyncContext ctx = request.startAsync();
        responseExecutor.add(this.getKeyStrategically(request), ctx);
    }

    private String getKeyStrategically(HttpServletRequest request) {
        // TODO もっと賢いキーの生成を
        return request.getParameter("key");
    }

}
