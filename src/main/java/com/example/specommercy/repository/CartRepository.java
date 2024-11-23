package com.example.specommercy.repository;

import com.example.specommercy.model.Cart;
import com.example.specommercy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Cart,Long> {
    @Query("SELECT c FROM Cart c WHERE c.user.username = ?1")
    Cart findCartByUsername(String username);

    @Query("SELECT c from Cart c WHERE c.user.email = ?1")
    Cart findCartByEmail(String emailId);
}
