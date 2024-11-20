package com.example.specommercy.service;

import com.example.specommercy.payload.ProductDTO;
import com.example.specommercy.payload.ProductResponse;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO productDTO);

    ProductResponse getAllProducts();
}
