package com.project.moneymanager.controllers;
import com.project.moneymanager.dto.ExpensesDto;
import com.project.moneymanager.dto.IncomeDto;
import com.project.moneymanager.service.IncomeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1.0/income")
public class IncomeController
{
    @Autowired
    private IncomeServices incomeServices;
    @PostMapping("/add")
    public ResponseEntity<IncomeDto> addIncome(@RequestBody IncomeDto incomeDto){
        IncomeDto saved=incomeServices.addIncome(incomeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/get")
    public ResponseEntity<List<IncomeDto>> getAllExpensesCurrentMonth()
    {
        List<IncomeDto> lst=incomeServices.getCurrentMonthIncome();
        return ResponseEntity.ok(lst);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<IncomeDto> deleteByExpense(@PathVariable Long id)
    {
        IncomeDto income=incomeServices.deleteIncomeEntity(id);
        return ResponseEntity.ok(income);

    }

    @GetMapping("/topfive")
    public ResponseEntity<List<IncomeDto>> getTop5()
    {
        List<IncomeDto> lst=incomeServices.getLatest5Income();
        return ResponseEntity.ok(lst);
    }


}
