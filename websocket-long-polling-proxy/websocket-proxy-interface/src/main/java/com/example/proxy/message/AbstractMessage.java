package com.example.proxy.message;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

public abstract class AbstractMessage implements Message {
    protected String category;
    private JsonNode message;

    public AbstractMessage() {
        this.category = this.getCategory();
        this.message = new TextNode("");
    }

    public AbstractMessage(final String message) {
        this.category = this.getCategory();
        this.message = new TextNode(message);
    }

    public AbstractMessage(final JsonNode message) {
        this.category = this.getCategory();
        this.message = message;
    }

    public abstract String getCategory();
    public abstract void setCategory(String category);

    @JsonProperty
    public  JsonNode getMessage() {
        return this.message;
    }

    public void setMessage(JsonNode message) {
        this.message = message;
    }
}
