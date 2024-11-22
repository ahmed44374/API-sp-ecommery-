package com.example.specommercy.repository;

import com.example.specommercy.model.AppRole;
import com.example.specommercy.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
     Optional<Role> findByRoleName(AppRole appRole);
}
