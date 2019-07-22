package com.example.vuespringexample.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
    @RequestMapping(value = "/api/greeting", method = RequestMethod.POST)
    public Greeting greeting() {
        return new Greeting("Hello!");
    }

    @RequestMapping(value = "/api/secure-greeting", method = RequestMethod.POST)
    public Greeting secureGreeting() {
        return new Greeting("Secure Hello!");
    }
}
