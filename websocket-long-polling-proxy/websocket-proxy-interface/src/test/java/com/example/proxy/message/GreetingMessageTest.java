package com.example.proxy.message;

import static org.junit.Assert.*;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class GreetingMessageTest {
    @Test
    public void testMappingToJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String messageRepresentation = mapper.writeValueAsString(new GreetingMessage());
        assertNotEquals(messageRepresentation, "");
        assertNotEquals(messageRepresentation, "{}");
        assertTrue(messageRepresentation.indexOf("category") > 0);
        assertTrue(messageRepresentation.indexOf("message") > 0);
    }
}
