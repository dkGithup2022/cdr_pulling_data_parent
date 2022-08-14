package com.dk0124.cdr.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HealthCheckController {
    @GetMapping("/api/ping")
    public String ping() {
        return "ok";
    }
}
