package com.example.specommercy.service;

import com.example.specommercy.exception.APIException;
import com.example.specommercy.exception.ResourceNotFoundException;
import com.example.specommercy.model.Category;
import com.example.specommercy.payload.CategoryDTO;
import com.example.specommercy.payload.CategoryResponse;
import com.example.specommercy.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper){
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        categoryRepository.findByCategoryName(categoryDTO.getCategoryName())
                .ifPresent(c -> { throw new APIException(String.format("Category with the name %s already exists!",c.getCategoryName())); });

        Category category = modelMapper.map(categoryDTO,Category.class);
        Category savedCategory =  categoryRepository.save(category);
        return modelMapper.map(category,CategoryDTO.class);
    }

    @Override
    public CategoryResponse getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()) {
            throw new APIException("No category created till now.");
        }
        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category,CategoryDTO.class))
                .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        return categoryResponse;
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category","CategoryId",categoryId) );
        categoryRepository.deleteById(categoryId);
        return "category deleted successfully";
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Category tempCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category", "CategoryId", categoryId) );
        categoryRepository.findByCategoryName(categoryDTO.getCategoryName()).ifPresent(c -> { throw new APIException(String.format("Category with the name %s already exists!",c.getCategoryName())); });
        tempCategory.setCategoryName(categoryDTO.getCategoryName());
        Category category =  categoryRepository.save(tempCategory);
        return modelMapper.map(category,CategoryDTO.class);
    }

}
