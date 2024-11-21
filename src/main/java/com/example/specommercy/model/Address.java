package com.example.specommercy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();

    public Address(String street, String buildingName, String city, String state, String country, String zipcode) {
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipcode = zipcode;
    }
}
