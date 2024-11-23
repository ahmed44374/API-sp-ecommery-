package com.example.specommercy.controller;

import com.example.specommercy.payload.AddressDTO;
import com.example.specommercy.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
        AddressDTO createdAddressDTO = addressService.createAddress(addressDTO);
        return new ResponseEntity<>(createdAddressDTO, HttpStatus.OK);

    }

    @GetMapping("/user/addresses")
    public ResponseEntity<List<AddressDTO>> getLoggedInUserAddresses() {
        List<AddressDTO> addressDTOS = addressService.getLoggedInUserAddresses();
        return new ResponseEntity<>(addressDTOS, HttpStatus.FOUND);
    }

    @GetMapping("/addresses")
     public ResponseEntity<List<AddressDTO>> getAllAddresses() {
        List<AddressDTO> addressDTOS = addressService.getAllAddresses();
        return new ResponseEntity<>(addressDTOS, HttpStatus.FOUND);
    }
    @PutMapping("/address/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId, @RequestBody AddressDTO addressDTO) {
        AddressDTO updatedAddressDTO = addressService.updateAddress(addressId,addressDTO);
        return new ResponseEntity<>(updatedAddressDTO,HttpStatus.OK);
    }

    @DeleteMapping("/address/{addressId}")
    public ResponseEntity<AddressDTO> deleteAddress(@PathVariable Long addressId) {
        AddressDTO addressDTO = addressService.deleteAddress(addressId);
        return new ResponseEntity<>(addressDTO,HttpStatus.OK);
    }
}
