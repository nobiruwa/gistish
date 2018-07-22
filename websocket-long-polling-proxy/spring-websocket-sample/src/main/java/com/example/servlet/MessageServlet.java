package com.example.servlet;

import com.example.websocket.SessionHandler;
import com.example.proxy.message.RequestMessage;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
public class MessageServlet {
    @Autowired
    private SessionHandler sessionHandler;

    @CrossOrigin(origins="*")
    @RequestMapping(value="/actions", method=RequestMethod.POST)
    protected void actions(@RequestBody RequestMessage message) {
        System.out.println("MessageServlet#actions(" + message.getKey() + " " + message.getMessage().toString() + ")");
        sessionHandler.sendToAllConnectedSessions(message);
    }
}
