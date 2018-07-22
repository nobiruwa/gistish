package com.example.proxy.message;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.UUID;

public abstract class AbstractIdentifiedMessage extends AbstractMessage {
    private String messageId;

    public AbstractIdentifiedMessage() {
        super();
    }

    public AbstractIdentifiedMessage(final String message) {
        super(message);
        this.messageId = UUID.randomUUID().toString();
    }

    public AbstractIdentifiedMessage(final JsonNode message) {
        super(message);
        this.messageId = UUID.randomUUID().toString();
    }

    @JsonProperty
    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
