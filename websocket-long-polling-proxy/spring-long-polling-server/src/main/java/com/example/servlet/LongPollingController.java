package com.example.servlet;

import com.example.proxy.message.GeneralErrorMessage;
import com.example.executor.ResponseExecutor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class LongPollingController {
    @Autowired
    private Properties properties;
    @Autowired
    private ResponseExecutor responseExecutor;
    private ObjectMapper mapper = new ObjectMapper();
    private static final Logger LOGGER = Logger.getLogger(LongPollingController.class.getName());

    @Bean
    private long timeout() {
        return Long.parseLong(properties.getProperty("rest.long-polling.timeout"));
    }

    @Async
    @CrossOrigin(origins="*")
    @RequestMapping(value="/subscribe", method=RequestMethod.GET)
    protected DeferredResult<ResponseEntity<String>> subscribe(@RequestParam String key) {
        LOGGER.info("LongPollingController#subscribe(" + key + ")");
        DeferredResult<ResponseEntity<String>> result = new DeferredResult<ResponseEntity<String>>(timeout());
        result.onTimeout(() -> {
                responseExecutor.remove(key);
                try {
                    String errorMessage = mapper.writeValueAsString(new GeneralErrorMessage("timeout"));
                    ResponseEntity<String> response = ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).header("Content-Type", "application/json").body(errorMessage);
                    result.setErrorResult(response);
                } catch (JsonProcessingException ex) {
                    ex.printStackTrace();
                }
            });
        responseExecutor.add(key, result);

        return result;
    }
}
