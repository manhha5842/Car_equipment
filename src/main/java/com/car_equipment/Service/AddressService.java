package com.car_equipment.Service;

import com.car_equipment.DTO.AddressDTO;
import com.car_equipment.Model.Address;
import com.car_equipment.Model.User;
import com.car_equipment.Repository.AddressRepository;
import com.car_equipment.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    // Lấy danh sách Address
    public List<AddressDTO> getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();
        return addresses.stream().map(AddressDTO::transferToDTO).collect(Collectors.toList());
    }

    // Xem chi tiết Address
    public AddressDTO getAddressById(String id) {
        Optional<Address> address = addressRepository.findById(id);
        return address.map(AddressDTO::transferToDTO).orElse(null);
    }

    // Thêm Address
    public AddressDTO addAddress(AddressDTO addressDTO) {
        Address address = new Address();
        address.setUnit_number(addressDTO.getUnitNumber());
        address.setStreet(addressDTO.getStreet());
        address.setDistrict(addressDTO.getDistrict());
        address.setCity(addressDTO.getCity());

        Optional<User> userOptional = userRepository.findById(addressDTO.getUserId());
        userOptional.ifPresent(address::setUser);

        Address savedAddress = addressRepository.save(address);
        return AddressDTO.transferToDTO(savedAddress);
    }

    // Sửa Address
    public AddressDTO updateAddress(String id, AddressDTO addressDTO) {
        Optional<Address> addressOptional = addressRepository.findById(id);
        if (addressOptional.isPresent()) {
            Address address = addressOptional.get();
            address.setUnit_number(addressDTO.getUnitNumber());
            address.setStreet(addressDTO.getStreet());
            address.setDistrict(addressDTO.getDistrict());
            address.setCity(addressDTO.getCity());

            Optional<User> userOptional = userRepository.findById(addressDTO.getUserId());
            userOptional.ifPresent(address::setUser);

            Address updatedAddress = addressRepository.save(address);
            return AddressDTO.transferToDTO(updatedAddress);
        }
        return null;
    }

    // Xoá Address
    public boolean deleteAddress(String id) {
        Optional<Address> addressOptional = addressRepository.findById(id);
        if (addressOptional.isPresent()) {
            addressRepository.deleteById(id);
            return true;
        }
        return false;
    }
}