package com.project.moneymanager.service;
import com.project.moneymanager.dto.ExpensesDto;
import com.project.moneymanager.entity.CategoryEntity;
import com.project.moneymanager.entity.ExpensesEntity;
import com.project.moneymanager.entity.ProfileEntity;
import com.project.moneymanager.repository.CategoryRepository;
import com.project.moneymanager.repository.ExpenseRepository;
import com.project.moneymanager.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Service
public class ExpensesServices
{
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileServices profileServices;

    @Autowired
    private CategoryRepository categoryRepository;

    public ExpensesEntity toEntity(ExpensesDto expensesDto, ProfileEntity profile, CategoryEntity categoryEntity)
    {
        return ExpensesEntity.builder()
                .date(expensesDto.getDate())
                .icon(expensesDto.getIcon())
                .amount(expensesDto.getAmount())
                .category(categoryEntity)
                .name(expensesDto.getName())
                .profile(profile)
                .build();

    }

    public ExpensesDto toDto(ExpensesEntity expensesEntity)
    {
        return ExpensesDto.builder()
                .id(expensesEntity.getId())
                .amount(expensesEntity.getAmount())
                .name(expensesEntity.getName())
                .categoryId(expensesEntity.getCategory()!=null?expensesEntity.getCategory().getId():null)
                .categoryName(expensesEntity.getCategory()!=null?expensesEntity.getCategory().getName():null)
                .icon(expensesEntity.getIcon())
                .date(expensesEntity.getDate())
                .createdAt(expensesEntity.getCreatedAt())
                .updatedAt(expensesEntity.getUpdatedAt())
                .build();
    }

    public ExpensesDto addExpense(ExpensesDto expensesDto)
    {
        ProfileEntity profile=profileServices.getCurrentProfile();
        System.out.println(expensesDto.getCategoryId());
        CategoryEntity category=categoryRepository.findById(expensesDto.getCategoryId())
                .orElseThrow(()->new RuntimeException("category with id not found "+expensesDto.getCategoryId()));
        ExpensesEntity expensesEntity=toEntity(expensesDto,profile,category);
        expensesEntity=expenseRepository.save(expensesEntity);
        return toDto(expensesEntity);
    }
    public List<ExpensesDto> getCurrentMonthExpenses()
    {
        ProfileEntity profile=profileServices.getCurrentProfile();
        LocalDate currentDate=LocalDate.now();
        LocalDate startDate=currentDate.withDayOfMonth(1);
        LocalDate endDate=currentDate.withDayOfMonth(currentDate.lengthOfMonth());
        List<ExpensesEntity> expensesEntities=expenseRepository.findByProfileIdAndDateBetween(profile.getId(),startDate,endDate);
        return expensesEntities.stream().map(this::toDto).toList();
    }

    public ExpensesDto deleteExpenseEntity(Long expenseId)
    {
        ProfileEntity profile=profileServices.getCurrentProfile();
        ExpensesEntity expense=expenseRepository.findById(expenseId)
                .orElseThrow(()->new RuntimeException("expense not found"));
        if(!expense.getProfile().getId().equals(profile.getId()))
        {
            throw new RuntimeException("Unthorized to use this expense");
        }
        expenseRepository.delete(expense);
        return toDto(expense);
    }
    public List<ExpensesDto> getLatest5Expenses(){
        ProfileEntity profile=profileServices.getCurrentProfile();
        List<ExpensesEntity> lst=expenseRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return lst.stream().map(this::toDto).toList();
    }

    public BigDecimal getAllExpenses()
    {
        ProfileEntity profile=profileServices.getCurrentProfile();
        BigDecimal total=expenseRepository.findTotalExpenseByProfileId(profile.getId());
        return total==null?BigDecimal.ZERO:total;
    }


    public List<ExpensesDto> filter(LocalDate startDate, LocalDate endDate, String keyword, Sort sort)
    {
        ProfileEntity profile=profileServices.getCurrentProfile();
        List<ExpensesEntity> lst=expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(),startDate,endDate,keyword,sort);
        return lst.stream().map(this::toDto).toList();

    }

    @Transactional
    public List<ExpensesDto> getExpenseForUserOnDate(Long profileId,LocalDate date){
        List<ExpensesEntity> lst=expenseRepository.findByProfileIdAndDate(profileId,date);
        return lst.stream().map(this::toDto).toList();
    }


}
