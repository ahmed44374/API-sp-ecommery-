package com.example.specommercy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class Testcontroller {

    @GetMapping
    public String sayHello() {
        return "test test";
    }
}
