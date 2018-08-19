package com.example;

import com.example.ApplicationConfig;
import com.example.servlet.ServletConfig;
import com.example.websocket.WebSocketConfig;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import org.springframework.web.context.ContextLoaderListener;

public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {
            ApplicationConfig.class,
            ServletConfig.class,
            WebSocketConfig.class
        };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {
            "/subscribe"
        };
    }
}
