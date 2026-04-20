package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping()
    public String nullmethod() {

        return "This is a null method.";
    }

    @GetMapping("/hello")
    public String hello() {

        return "Hello Dinesh! Spring Boot API working.";

    }

}