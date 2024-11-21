package com.example.specommercy.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long categoryId;
    @NotBlank(message = "category name must not be blank")
    @Size(min = 5, message = "category name must contain at least 5 characters")
    private String categoryName;
}
