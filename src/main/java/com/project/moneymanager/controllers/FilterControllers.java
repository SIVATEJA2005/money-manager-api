package com.project.moneymanager.controllers;
import com.project.moneymanager.dto.ExpensesDto;
import com.project.moneymanager.dto.FilterDto;
import com.project.moneymanager.dto.IncomeDto;
import com.project.moneymanager.service.ExpensesServices;
import com.project.moneymanager.service.IncomeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.List;
@RestController
@RequestMapping("/api/v1.0/filter")
public class FilterControllers
{
    @Autowired
    private ExpensesServices expensesServices;
    @Autowired
    private IncomeServices incomeServices;
    @PostMapping
    public ResponseEntity<?> filter(@RequestBody  FilterDto filterDto)
    {
//        System.out.println(filterDto.getType());
        LocalDate startDate=filterDto.getStartDate()==null?LocalDate.MIN:filterDto.getStartDate();
        LocalDate endDate=filterDto.getEndDate()==null?LocalDate.now():filterDto.getEndDate();
        String keyword= filterDto.getKeyword()==null?"": filterDto.getKeyword();
        String sortField=filterDto.getSortField()==null?"date":filterDto.getSortField();
        Sort.Direction sortOrder="desc".equalsIgnoreCase(filterDto.getSortOrder())?Sort.Direction.DESC:Sort.Direction.ASC;
        Sort sort=Sort.by(sortOrder,sortField);
        if("expense".equalsIgnoreCase(filterDto.getType()))
        {
            List<ExpensesDto> expenses=expensesServices.filter(startDate,endDate,keyword,sort);
            return ResponseEntity.ok(expenses);
        }
        else if("income".equalsIgnoreCase(filterDto.getType()))
        {
            List<IncomeDto> income=incomeServices.filter(startDate,endDate,keyword,sort);
            return ResponseEntity.ok(income);
        }
        else
        {
           return ResponseEntity.badRequest().body("Invalid type.type should be either income or expense");
        }


    }

}
