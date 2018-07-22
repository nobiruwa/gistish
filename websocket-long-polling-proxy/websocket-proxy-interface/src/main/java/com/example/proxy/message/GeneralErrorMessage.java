package com.example.proxy.message;

import com.fasterxml.jackson.annotation.*;

public class GeneralErrorMessage extends AbstractMessage {
    @JsonIgnore
    public static final String CATEGORY = "error";

    public GeneralErrorMessage() {
        super();
    }

    public GeneralErrorMessage(final String message) {
        super(message);
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
