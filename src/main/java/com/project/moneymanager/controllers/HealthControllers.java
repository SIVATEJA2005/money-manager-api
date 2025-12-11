package com.project.moneymanager.controllers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/status","/health"})
public class HealthControllers {

    @GetMapping
    public String getHealth()
    {
        return "application is running";
    }


}
