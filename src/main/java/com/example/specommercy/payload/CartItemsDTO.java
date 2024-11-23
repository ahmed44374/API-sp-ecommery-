package com.example.specommercy.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemsDTO {
    private Long cartItemId;
    @JsonIgnore
    private CartDTO cart;
    private ProductDTO productDTO;
    private Integer quantity;
    private Double discount;
    private Double productPrice;

}
