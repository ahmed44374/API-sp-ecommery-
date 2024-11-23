package com.example.specommercy.service;

import com.example.specommercy.model.User;
import com.example.specommercy.payload.AddressDTO;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO);

    List<AddressDTO> getAllAddresses();

    List<AddressDTO> getLoggedInUserAddresses();

    AddressDTO updateAddress(Long addressId, AddressDTO addressDTO);

    AddressDTO deleteAddress(Long addressId);
}
