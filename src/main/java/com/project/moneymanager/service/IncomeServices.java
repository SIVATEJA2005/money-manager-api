package com.project.moneymanager.service;
import com.project.moneymanager.dto.ExpensesDto;
import com.project.moneymanager.dto.IncomeDto;
import com.project.moneymanager.entity.CategoryEntity;
import com.project.moneymanager.entity.ExpensesEntity;
import com.project.moneymanager.entity.IncomeEntity;
import com.project.moneymanager.entity.ProfileEntity;
import com.project.moneymanager.repository.CategoryRepository;
import com.project.moneymanager.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class IncomeServices {

    @Autowired
    private IncomeRepository incomeRepository;
    @Autowired
    private ProfileServices profileServices;
    @Autowired
    private CategoryRepository categoryRepository;
    public IncomeEntity toEntity(IncomeDto incomeDto, ProfileEntity profile, CategoryEntity categoryEntity)
    {
        return IncomeEntity.builder()
                .id(incomeDto.getId())
                .date(incomeDto.getDate())
                .icon(incomeDto.getIcon())
                .amount(incomeDto.getAmount())
                .category(categoryEntity)
                .name(incomeDto.getName())
                .profile(profile)
                .build();

    }
    public IncomeDto toDto(IncomeEntity incomeEntity)
    {
        return IncomeDto.builder()
                .id(incomeEntity.getId())
                .amount(incomeEntity.getAmount())
                .name(incomeEntity.getName())
                .categoryId(incomeEntity.getCategory()!=null?incomeEntity.getCategory().getId():null)
                .categoryName(incomeEntity.getCategory()!=null?incomeEntity.getCategory().getName():null)
                .icon(incomeEntity.getIcon())
                .date(incomeEntity.getDate())
                .createdAt(incomeEntity.getCreatedAt())
                .updatedAt(incomeEntity.getUpdatedAt())
                .build();
    }
    public IncomeDto addIncome(IncomeDto incomeDto)
    {

        ProfileEntity profile=profileServices.getCurrentProfile();
        CategoryEntity category=categoryRepository.findById(incomeDto.getCategoryId())
                .orElseThrow(()->new RuntimeException("category with id not found "+incomeDto.getCategoryId()));
        IncomeEntity incomeEntity=toEntity(incomeDto,profile,category);
        incomeEntity=incomeRepository.save(incomeEntity);
        return toDto(incomeEntity);
    }
    public List<IncomeDto> getCurrentMonthIncome()
    {
        ProfileEntity profile=profileServices.getCurrentProfile();
        LocalDate currentDate=LocalDate.now();
        LocalDate startDate=currentDate.withDayOfMonth(1);
        LocalDate endDate=currentDate.withDayOfMonth(currentDate.lengthOfMonth());
        List<IncomeEntity> incomeEntities=incomeRepository.findByProfileIdAndDateBetween(profile.getId(),startDate,endDate);
        return incomeEntities.stream().map(this::toDto).toList();
    }
    public IncomeDto deleteIncomeEntity(Long incomeId)
    {
        ProfileEntity profile=profileServices.getCurrentProfile();
        IncomeEntity income=incomeRepository.findById(incomeId)
                .orElseThrow(()->new RuntimeException("expense not found"));
        if(!income.getProfile().getId().equals(profile.getId()))
        {
            throw new RuntimeException("Unthorized to use this expense");
        }
        incomeRepository.delete(income);
        return toDto(income);
    }
    public List<IncomeDto> getLatest5Income(){
        ProfileEntity profile=profileServices.getCurrentProfile();
        List<IncomeEntity> lst=incomeRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return lst.stream().map(this::toDto).toList();
    }
    public BigDecimal getAllIncome()
    {
        ProfileEntity profile=profileServices.getCurrentProfile();
        BigDecimal total=incomeRepository.findTotalIncomeByProfileId(profile.getId());
        return total==null?BigDecimal.ZERO:total;
    }
    public List<IncomeDto> filter(LocalDate startDate, LocalDate endDate, String keyword, Sort sort)
    {
        ProfileEntity profile=profileServices.getCurrentProfile();
        List<IncomeEntity> lst=incomeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(),startDate,endDate,keyword,sort);
        return lst.stream().map(this::toDto).toList();
    }

    public ByteArrayInputStream downloadIncomeExcel() throws IOException {
        ProfileEntity profile = profileServices.getCurrentProfile();
        List<IncomeEntity> incomeList = incomeRepository.findByProfileId(profile.getId());
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Income Details");
            // Header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Name", "Category", "Amount", "Date", "Created At"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                // Bold style for header
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }
            // Data rows
            int rowIdx = 1;
            for (IncomeEntity income : incomeList) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(income.getId());
                row.createCell(1).setCellValue(income.getName());
                row.createCell(2).setCellValue(income.getCategory() != null ? income.getCategory().getName() : "");
                row.createCell(3).setCellValue(income.getAmount().doubleValue());
                row.createCell(4).setCellValue(income.getDate() != null ? income.getDate().toString() : "");
                row.createCell(5).setCellValue(income.getCreatedAt() != null ? income.getCreatedAt().toString() : "");
            }
            // Auto size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }


}
