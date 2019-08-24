package com.example.springdatajpaexample.controller;

import com.example.springdatajpaexample.service.LeftJoinOnCol1Col3Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DemoController {
    @Autowired
    private LeftJoinOnCol1Col3Service leftJoinOnCol1Col3Service;

    @RequestMapping("/")
    public void run() {
        leftJoinOnCol1Col3Service.findAll().stream().forEach(System.out::println);
    }
}
