package com.example.specommercy.service;

import com.example.specommercy.model.Category;

import java.util.List;

public interface CategoryService {
    public void addCategory(Category category);
    public List<Category> getAllCategories();

    public String deleteCategory(Long categoryId);

    public Category updateCategory(Long categoryId, Category category);
}
