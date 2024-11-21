package com.example.specommercy.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long productId;
    @NotBlank(message = "product name must not be blank")
    @Size(min = 3, message = "product name must contain at least 3 characters")
    private String productName;
    private String image;
    @Size(min = 10, message = "product description must contain at least 10 characters")
    private String description;
    private Integer quantity;
    private Double price;
    private Double specialPrice;
    private Double discount;
}
