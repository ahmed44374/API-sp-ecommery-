package com.example.specommercy.service;

import com.example.specommercy.model.Category;
import com.example.specommercy.payload.CategoryDTO;
import com.example.specommercy.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {
     CategoryDTO addCategory(CategoryDTO categoryDTO);
    CategoryResponse getAllCategories();
     String deleteCategory(Long categoryId);
     CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO);
}
