package com.project.moneymanager.controllers;
import com.project.moneymanager.dto.CategoryDto;
import com.project.moneymanager.service.CategoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.Map;
@RestController
@RequestMapping("/api/v1.0/categories")
public class CatogoryControllers
{
    @Autowired
    private CategoryServices categoryServices;
    @PostMapping
    public ResponseEntity<CategoryDto> addCateogry(@RequestBody CategoryDto categoryDto){
        CategoryDto savedCategory=categoryServices.saveCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }
    @GetMapping
    public ResponseEntity<List<CategoryDto>>  getAllCategories(){
        List<CategoryDto> lst=categoryServices.getAllCategoryForCUrrentUser();
        return ResponseEntity.ok(lst);
    }

    @GetMapping("/get/{type}")
    public ResponseEntity<List<CategoryDto>> getAllCategoriesOfType(@PathVariable String type) {
        List<CategoryDto> lst = categoryServices.getCategoriesBasedOnType(type);
        return ResponseEntity.ok(lst);
    }
    @PutMapping("/update/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long categoryId,@RequestBody CategoryDto categoryDto)
    {
        CategoryDto updateCategory=categoryServices.updateCategory(categoryId,categoryDto);
        return ResponseEntity.ok(updateCategory);
    }

}
