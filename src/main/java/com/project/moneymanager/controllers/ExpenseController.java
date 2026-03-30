package com.project.moneymanager.controllers;
import com.project.moneymanager.dto.ExpensesDto;
import com.project.moneymanager.service.ExpensesServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1.0/expense")
public class ExpenseController
{

    @Autowired
    private ExpensesServices expensesServices;

    @PostMapping("/add")
    public ResponseEntity<ExpensesDto> addExpense(@RequestBody ExpensesDto expensesDto)
    {
        ExpensesDto saved=expensesServices.addExpense(expensesDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/get")
    public ResponseEntity<List<ExpensesDto>> getAllExpensesCurrentMonth(){
        List<ExpensesDto> lst=expensesServices.getCurrentMonthExpenses();
        return ResponseEntity.ok(lst);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ExpensesDto> deleteByExpense(@PathVariable Long id){
        ExpensesDto expense=expensesServices.deleteExpenseEntity(id);
        return ResponseEntity.ok(expense);
    }


}
