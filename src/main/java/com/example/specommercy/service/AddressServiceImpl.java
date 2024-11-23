package com.example.specommercy.service;

import com.example.specommercy.exception.APIException;
import com.example.specommercy.model.Address;
import com.example.specommercy.model.User;
import com.example.specommercy.payload.AddressDTO;
import com.example.specommercy.repository.AddressRepository;
import com.example.specommercy.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService{
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;
    private final AuthUtil authUtil;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, ModelMapper modelMapper, AuthUtil authUtil) {
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
        this.authUtil = authUtil;
    }

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {
        Address address = modelMapper.map(addressDTO, Address.class);
        User user = authUtil.loggedInUser();
        List<Address> addressList = user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress,AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getLoggedInUserAddresses() {
        List<Address> addresses = addressRepository.findByUser(authUtil.loggedInUser())
                .orElseThrow(() -> new APIException("user doesn't set any addresses"));
        return addresses.stream()
                .map(address -> modelMapper.map(address,AddressDTO.class))
                .toList();
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();
        if(addresses.isEmpty())
            throw new APIException("No addresses found");
        return addresses.stream()
                .map(address -> modelMapper.map(address,AddressDTO.class))
                .toList();
    }
}
