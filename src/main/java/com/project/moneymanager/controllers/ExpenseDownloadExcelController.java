package com.project.moneymanager.controllers;


import com.project.moneymanager.service.ExpensesServices;
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
public class ExpenseDownloadExcelController {

    @Autowired
    private ExpensesServices expensesServices;

    @GetMapping("/excel/download/expense")
    public ResponseEntity<InputStreamResource> downloadExpenseExcel() throws IOException {
        ByteArrayInputStream data = expensesServices.downloadExpenseExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=expense_details.xlsx");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(data));
    }
}
