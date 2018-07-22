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

@WebServlet(name="MessageServlet", urlPatterns={ "/message" }, asyncSupported=true)
public class MessageServlet extends HttpServlet {
    @Inject
    private ResponseExecutor responseExecutor;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        responseExecutor.sendIfAvailable(this.getKeyStrategically(request), this.getMessageStrategically(request));
    }

    private String getKeyStrategically(HttpServletRequest request) {
        // TODO もっと賢いキーの生成を
        return request.getParameter("key");
    }

    private String getMessageStrategically(HttpServletRequest request) {
        // TODO もっと賢いメッセージの取得を
        return request.getParameter("message");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TODO GETよりも高度なメッセージング処理を
    }
}
