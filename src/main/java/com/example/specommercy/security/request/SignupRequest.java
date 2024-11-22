package com.example.specommercy.security.request;

import java.util.Set;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @Getter
    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    public void setRole(Set<String> role) {
        this.role = role;
    }
}