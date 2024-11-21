package com.example.specommercy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    @NotBlank(message = "product name must not be blank")
    @Size(min = 3, message = "product name must contain at least 3 characters")
    private String productName;
    private String image;
    @NotBlank
    @Size(min = 10, message = "product description must contain at least 10 characters")
    private String description;
    private Integer quantity;
    private Double price;
    private Double specialPrice;
    private Double discount;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
