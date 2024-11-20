package com.example.specommercy.controller;

import com.example.specommercy.config.AppConstants;
import com.example.specommercy.payload.CategoryDTO;
import com.example.specommercy.payload.CategoryResponse;
import com.example.specommercy.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class categoryController {
    private final CategoryService categoryService;
    @Autowired
    public categoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER,        required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",   defaultValue = AppConstants.PAGE_SIZE,          required = false) Integer pageSize,
            @RequestParam(name = "sortBy",     defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder",  defaultValue = AppConstants.SORT_DIRECTION,     required = false) String sortOrder

    ) {
        return new ResponseEntity<>(categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder),HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDTO> addCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategory = categoryService.addCategory(categoryDTO);
        return new ResponseEntity<>(savedCategory,HttpStatus.CREATED) ;
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {
            CategoryDTO deletedCategory =  categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>(deletedCategory, HttpStatus.OK);
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<String> UpdateCategory(@PathVariable Long categoryId,@Valid @RequestBody CategoryDTO categoryDTO) {
            CategoryDTO status =  categoryService.updateCategory(categoryId,categoryDTO);
            return new ResponseEntity<>("category updated " + categoryDTO.getCategoryName(), HttpStatus.OK);
    }


}
