package com.example.specommercy.controller;

import com.example.specommercy.model.Category;
import com.example.specommercy.payload.CategoryDTO;
import com.example.specommercy.payload.CategoryResponse;
import com.example.specommercy.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class categoryController {
    private final CategoryService categoryService;
    @Autowired
    public categoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories() {
        return new ResponseEntity<>(categoryService.getAllCategories(),HttpStatus.OK);
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
