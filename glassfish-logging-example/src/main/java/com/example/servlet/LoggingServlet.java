package com.example.servlet;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// import java.util.logging.Level;
// import java.util.logging.Logger;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

@RestController
public class LoggingServlet {
    private final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LoggingServlet.class.getName());
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(LoggingServlet.class);

    @CrossOrigin(origins="*")
    @RequestMapping(value="/logging", method=RequestMethod.GET)
    protected ResponseEntity<String> subscribe(@RequestParam String key) {
        String logMessage = "MessageServlet#subscribe(" + key + ")";
        System.out.println("System.out.println: " + logMessage);
        logger.info("java.util.logging.Logger: INFO: " + logMessage);
        logger.warning("java.util.logging.Logger: WARN: " + logMessage);
        logger.severe("java.util.logging.Logger: SEVERE: " + logMessage);
        LOGGER.info("org.slf4j.Logger: INFO: " + logMessage);
        LOGGER.warn("org.slf4j.Logger: WARN: " + logMessage);
        LOGGER.error("org.slf4j.Logger: ERROR: " + logMessage);
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).header("Content-Type", "application/json").body("{\"key\":\"" + key + "\"}");
        return response;
    }
}
