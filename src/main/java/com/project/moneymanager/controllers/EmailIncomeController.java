package com.project.moneymanager.controllers;

import com.project.moneymanager.entity.ProfileEntity;
import com.project.moneymanager.service.EmailServices;
import com.project.moneymanager.service.IncomeServices;
import com.project.moneymanager.service.ProfileServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/v1.0")
public class EmailIncomeController {

    @Autowired
    private EmailServices emailServices;

    @Autowired
    private IncomeServices incomeServices;

    @Autowired
    private ProfileServices profileServices;

    @GetMapping("/email/income-excel")
    public ResponseEntity<String> emailIncomeExcel() {
        try {
            ProfileEntity profile = profileServices.getCurrentProfile();
            String toEmail = profile.getEmail();

            ByteArrayInputStream excelStream = incomeServices.downloadIncomeExcel();
            byte[] excelBytes = excelStream.readAllBytes();

            emailServices.sendIncomeExcel(toEmail, excelBytes);

            return ResponseEntity.ok("Income details emailed successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to send email: " + e.getMessage());
        }
    }
}