package com.example.proxy.message;

import com.fasterxml.jackson.annotation.*;

public class ProxyMessage extends AbstractIdentifiedMessage {
    @JsonIgnore
    public static final String CATEGORY = "proxy";

    private String key;

    public ProxyMessage() {
    }

    public ProxyMessage(String key, String message) {
        super(message);
        this.key = key;
    }

    public ProxyMessage(RequestMessage requestMessage) {
        super(requestMessage.getMessage());
        this.key = requestMessage.getKey();
    }

    @Override
    @JsonProperty
    public String getCategory() {
        return CATEGORY;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
