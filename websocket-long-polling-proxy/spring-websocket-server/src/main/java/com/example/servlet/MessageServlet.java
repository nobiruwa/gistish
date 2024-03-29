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

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class MessageServlet {
    @Autowired
    private SessionHandler sessionHandler;
    private static final Logger LOGGER = Logger.getLogger(MessageServlet.class.getName());

    @CrossOrigin(origins="*")
    @RequestMapping(value="/actions", method=RequestMethod.POST)
    protected void actions(@RequestBody RequestMessage message) {
        LOGGER.info("MessageServlet#actions(" + message.getKey() + " " + message.getMessage().toString() + ")");
        sessionHandler.sendToAllConnectedSessions(message);
    }
}
