package com.example.specommercy.service;

import com.example.specommercy.payload.CartDTO;

import java.util.List;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCart(String emailId, Long cartID);

    CartDTO getLoggedInUserCart();
}
