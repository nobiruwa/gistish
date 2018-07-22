package com.example.proxy.message;

import com.fasterxml.jackson.annotation.*;

public class RequestMessage extends AbstractMessage {
    @JsonIgnore
    public static final String CATEGORY = "request";

    private String key;

    public RequestMessage() {
        super();
    }

    public RequestMessage(String key, String message) {
        super(message);
        this.key = key;
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
