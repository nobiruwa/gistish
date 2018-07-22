package com.example.proxy.message;

import com.fasterxml.jackson.annotation.*;

public class GreetingMessage extends AbstractMessage {
    @JsonIgnore
    public static final String CATEGORY = "greeting";

    public GreetingMessage() {
        super("Hello");
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
}
