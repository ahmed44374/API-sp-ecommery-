package com.example.specommercy.payload;

import com.example.specommercy.model.User;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    private Long addressId;
    @NotBlank
    @Size(min = 5, message = "street name must be at least 5 characters")
    private String street;

    @NotBlank
    @Size(min = 3, message = "building name must be at least 3 characters")
    private String buildingName;

    @Size(min = 3, message = "city name must be at least 3 characters")
    private String city;

    @Size(min = 2, message = "state name must be at least 2 characters")
    private String state;

    @Size(min = 3, message = "country name must be at least 3 characters")
    private String country;

    @Size(min = 3, message = "zipcode name must be at least 3 characters")
    private String zipcode;
}
