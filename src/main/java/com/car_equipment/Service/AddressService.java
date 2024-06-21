package com.car_equipment.Service;

import com.car_equipment.DTO.AddressDTO;
import com.car_equipment.Model.Address;
import com.car_equipment.Model.EnumAddressStatus;
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

    // Lấy danh sách địa chỉ của user
    public List<AddressDTO> getAddressesByUserId(String userId) {
        List<Address> addresses = addressRepository.findByUserId(userId);
        return addresses.stream().map(AddressDTO::transferToDTO).collect(Collectors.toList());
    }

    public List<AddressDTO> getAddressesActiveByUserId(String userId) {
        List<Address> addresses = addressRepository.findByUserIdAndStatusNot(userId, "DELETED");
        return addresses.stream().map(AddressDTO::transferToDTO).collect(Collectors.toList());
    }

    // Xem chi tiết địa chỉ
    public AddressDTO getAddressById(String id) {
        Optional<Address> address = addressRepository.findById(id);
        return address.map(AddressDTO::transferToDTO).orElse(null);
    }

    // Thêm địa chỉ
    public AddressDTO addAddress(AddressDTO addressDTO) {
        Address address = AddressDTO.transferToEntity(addressDTO);
        address.setStatus(EnumAddressStatus.ACTIVE);
        return AddressDTO.transferToDTO(addressRepository.save(address));

    }

    // Sửa địa chỉ
    public AddressDTO updateAddress(String id, AddressDTO addressDTO) {
        Optional<Address> addressOptional = addressRepository.findById(id);
        if (addressOptional.isPresent()) {
            Address address = addressOptional.get();
            address.setName(addressDTO.getName());
            address.setAddressDetail(addressDTO.getAddressDetail());
            address.setDistrict(addressDTO.getDistrict());
            address.setWard(addressDTO.getWard());
            address.setProvince(addressDTO.getProvince());
            address.setLatitude(addressDTO.getLatitude());
            address.setLongitude(addressDTO.getLongitude());
            address.setIsDefault(addressDTO.getIsDefault());

            Address updatedAddress = addressRepository.save(address);
            return AddressDTO.transferToDTO(updatedAddress);
        }
        return null;
    }

    // Xoá địa chỉ
    public boolean deleteAddress(String id) {
        Optional<Address> addressOptional = addressRepository.findById(id);
        if (addressOptional.isPresent()) {
            Address address = addressOptional.get();
            address.setStatus(EnumAddressStatus.DELETED);
            addressRepository.save(address);
            return true;
        }
        return false;
    }
}