package com.example.specommercy.controller;

import com.example.specommercy.payload.CartDTO;
import com.example.specommercy.repository.CartRepository;
import com.example.specommercy.service.CartService;
import com.example.specommercy.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {
    private final CartService cartService;
    @Autowired
    public CartController(CartService cartService, AuthUtil authUtil) {
        this.cartService = cartService;
    }

    @PostMapping("/cart/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId,
                                                    @PathVariable Integer quantity){
        CartDTO cartDTO = cartService.addProductToCart(productId,quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }
    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        List<CartDTO> cartDTOS = cartService.getAllCarts();
        return new ResponseEntity<>(cartDTOS,HttpStatus.FOUND);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getUserCartById() {
        CartDTO cartDTO = cartService.getLoggedInUserCart();
        return new ResponseEntity<>(cartDTO,HttpStatus.FOUND);
    }
}
