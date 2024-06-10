package com.car_equipment.DTO;

import com.car_equipment.Model.Address;
import lombok.Data;

@Data
public class AddressDTO {
    private String id;
    private int unitNumber;
    private String street;
    private String district;
    private String city;
    private String userId;

    public static AddressDTO transferToDTO(Address address) {
        AddressDTO dto = new AddressDTO();
        dto.setId(address.getId());
        dto.setUnitNumber(address.getUnit_number());
        dto.setStreet(address.getStreet());
        dto.setDistrict(address.getDistrict());
        dto.setCity(address.getCity());
        dto.setUserId(address.getUser().getId());
        return dto;
    }
}