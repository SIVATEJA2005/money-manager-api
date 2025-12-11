package com.project.moneymanager.controllers;

import com.project.moneymanager.service.DashBoardServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashBoardController
{
    @Autowired
    private DashBoardServices dashBoardServices;

    @GetMapping
    public ResponseEntity<Map<String,Object>> getDashBoard()
    {
        Map<String,Object> dashBoardData=dashBoardServices.getDashBoardData();
        return ResponseEntity.ok(dashBoardData);
    }

}
