package com.example.specommercy.controller;
import com.example.specommercy.model.CartItem;
import com.example.specommercy.payload.CartDTO;
import com.example.specommercy.service.CartService;
import com.example.specommercy.util.AuthUtil;
import jakarta.validation.constraints.Min;
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
                                                    @Min(1) @PathVariable Integer quantity){
         System.out.println( "lol1");
        CartDTO cartDTO = cartService.addProductToCart(productId,quantity);
        System.out.println( "lol2");

        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }
    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        List<CartDTO> cartDTOS = cartService.getAllCarts();
        return new ResponseEntity<>(cartDTOS,HttpStatus.FOUND);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getLoggedInUserCart() {
        CartDTO cartDTO = cartService.getLoggedInUserCart();
        return new ResponseEntity<>(cartDTO,HttpStatus.FOUND);
    }
    
    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Integer operation, @Min(1) @PathVariable Long productId) {
        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId,operation);
        return new ResponseEntity<>(cartDTO,HttpStatus.OK);
    }
    
    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId,
                                                        @PathVariable Long productId) {
        String status = cartService.deleteProductFromCart(cartId, productId);

        return new ResponseEntity<String>(status, HttpStatus.OK);
    }
}
