package com.example.specommercy.service;

import com.example.specommercy.payload.CategoryDTO;
import com.example.specommercy.payload.CategoryResponse;

public interface CategoryService {
     CategoryDTO addCategory(CategoryDTO categoryDTO);
    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
     CategoryDTO deleteCategory(Long categoryId);
     CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO);
}
