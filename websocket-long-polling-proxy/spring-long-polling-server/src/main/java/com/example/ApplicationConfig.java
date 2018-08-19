package com.example;

import java.io.IOException;
import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;

import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
@ComponentScan(basePackages = {"com.example.configuration"})
public class ApplicationConfig {
    private static final Logger LOGGER = Logger.getLogger(ApplicationConfig.class.getName());

    @Bean
    public Properties properties() {
        try {
            LOGGER.info("ApplicationConfig#properties() loading properties...");
            Properties properties = new Properties();
            properties.load(this.getClass().getResourceAsStream("/websocket-client.properties"));
            return properties;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
