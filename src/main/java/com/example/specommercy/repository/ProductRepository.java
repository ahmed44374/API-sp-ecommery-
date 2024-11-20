package com.example.specommercy.repository;

import com.example.specommercy.model.Category;
import com.example.specommercy.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findProductByCategory(Category category);

    List<Product> findByProductNameLikeIgnoreCase(String keyword);
}
