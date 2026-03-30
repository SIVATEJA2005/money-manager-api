package com.project.moneymanager.controllers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1.0") // Match the prefix used in SecurityConfig
public class HealthControllers {

    @GetMapping("/status")
    public String getHealth() {
        return "application is running";
    }

    @GetMapping("/health") // Add this so your test URL actually exists
    public String getTest() {
        return "Test successful";
    }
}
