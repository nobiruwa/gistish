package com.example.springdatajpaexample.entity;

import java.util.HashMap;

import com.google.gson.GsonBuilder;

public class ToStringWrapper extends HashMap<String, Object> {
    private static final long serialVersionUID = -1L;

    public ToStringWrapper(String key, Object value) {
        super();
        this.put(key, value);
    }

    public String toString() {
        return (new GsonBuilder().serializeNulls().create()).toJson(this);
    }
}
