package com.project.moneymanager.controllers;

import com.project.moneymanager.service.IncomeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1.0")
public class IncomeDownloadExcelController {

    @Autowired
    private IncomeServices incomeServices;

    @GetMapping("/excel/download/income")
    public ResponseEntity<InputStreamResource> downloadIncomeExcel() throws IOException {
        ByteArrayInputStream data = incomeServices.downloadIncomeExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=income_details.xlsx");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(data));
    }
}
