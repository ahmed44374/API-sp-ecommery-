package com.example.specommercy.service;

import com.example.specommercy.exception.APIException;
import com.example.specommercy.exception.ResourceNotFoundException;
import com.example.specommercy.model.Category;
import com.example.specommercy.model.Product;
import com.example.specommercy.payload.ProductDTO;
import com.example.specommercy.payload.ProductResponse;
import com.example.specommercy.repository.CategoryRepository;
import com.example.specommercy.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        double specialPrice = productDTO.getPrice() -
                ((productDTO.getDiscount() * 0.01) * productDTO.getPrice());

        Product product = modelMapper.map(productDTO , Product.class);
        product.setImage("default.png");
        product.setCategory(category);
        product.setSpecialPrice(specialPrice);
        product.setProductId(null);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(product,ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> products = productRepository.findAll();
        if(products.isEmpty()) throw new APIException("No products created till now.");
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }
}
