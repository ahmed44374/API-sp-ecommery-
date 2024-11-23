package com.example.specommercy.repository;

import com.example.specommercy.model.Address;
import com.example.specommercy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address,Long> {
    Optional<List<Address>> findByUser(User user);
}
