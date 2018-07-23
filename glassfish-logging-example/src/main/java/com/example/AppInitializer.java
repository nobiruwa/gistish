package com.example;

import com.example.servlet.ServletConfig;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import org.springframework.web.context.ContextLoaderListener;

public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {
            ServletConfig.class
        };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {
            "/logging"
        };
    }
}
