package com.example.specommercy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @NotBlank(message = "category name must not be blank")
    @Size(min = 5, message = "category name must contain at least 5 characters")

    private String categoryName;

    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
