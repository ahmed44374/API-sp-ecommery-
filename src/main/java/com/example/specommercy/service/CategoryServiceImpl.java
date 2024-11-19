package com.example.specommercy.service;

import com.example.specommercy.model.Category;
import com.example.specommercy.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void addCategory(Category category) {
        category.setCategoryId(null);
        categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"category not found") );
        categoryRepository.deleteById(categoryId);
        return "category deleted successfully";
    }

    @Override
    public Category updateCategory(Long categoryId, Category category) {
        Category tempCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"category not found") );;
        tempCategory.setCategoryName(category.getCategoryName());
        return categoryRepository.save(tempCategory);
    }

}
