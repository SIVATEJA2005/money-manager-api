package com.project.moneymanager.service;

import com.project.moneymanager.dto.ExpensesDto;
import com.project.moneymanager.dto.IncomeDto;
import com.project.moneymanager.dto.RecentTranscationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class DashBoardServices {

    @Autowired
    private  IncomeServices incomeServices;

    @Autowired
    private ExpensesServices expensesServices;

    @Autowired
    private ProfileServices profileServices;

    public Map<String,Object> getDashBoardData()
    {
        Map<String,Object> returnValue=new LinkedHashMap<>();
        List<IncomeDto> lastestIncome=incomeServices.getLatest5Income();
        List<ExpensesDto> lastestExpenses=expensesServices.getLatest5Expenses();
        List<RecentTranscationDto> recentTransactionDtos= Stream.concat(
            lastestIncome.stream().map(income->
                    RecentTranscationDto.builder()
                            .id(income.getId())
                            .icon(income.getIcon())
                            .type("income")
                            .name(income.getName())
                            .date(income.getDate())
                            .createdAt(income.getCreatedAt())
                            .updatedAt(income.getUpdatedAt())
                            .amount(income.getAmount())
                            .build()),
                lastestExpenses.stream().map(expense->
                        RecentTranscationDto.builder()
                                .id(expense.getId())
                                .icon(expense.getIcon())
                                .type("expense")
                                .name(expense.getName())
                                .date(expense.getDate())
                                .createdAt(expense.getCreatedAt())
                                .updatedAt(expense.getUpdatedAt())
                                .amount(expense.getAmount())
                                .build())

                        ).sorted((a,b)-> {
            int cmp = b.getDate().compareTo(a.getDate());
            if (cmp == 0) {
                return b.getCreatedAt().compareTo(a.getCreatedAt());
            }
            return cmp;
        }).toList();
        returnValue.put("totalBalance",incomeServices.getAllIncome()
                .subtract(expensesServices.getAllExpenses()));
        returnValue.put("totalIncome",incomeServices.getAllIncome());
        returnValue.put("totalExpense",expensesServices.getAllExpenses());
        returnValue.put("recentTop5Expenses",lastestExpenses);
        returnValue.put("recentTop5Incomes",lastestIncome);
        returnValue.put("recentTransactions",recentTransactionDtos);
        return returnValue;
    }


}
