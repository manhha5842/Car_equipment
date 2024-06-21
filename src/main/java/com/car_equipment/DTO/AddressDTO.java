package com.car_equipment.DTO;

import com.car_equipment.Model.Address;
import com.car_equipment.Model.User;
import com.car_equipment.Service.UserService;
import lombok.Data;

@Data
public class AddressDTO {
    private String id;
    private String name;
    private String addressDetail;
    private String district;
    private String ward;
    private String province;
    private String latitude;
    private String longitude;
    private Boolean isDefault;
    private String userId;

    public static AddressDTO transferToDTO(Address address) {
        AddressDTO dto = new AddressDTO();
        dto.setId(address.getId());
        dto.setName(address.getName());
        dto.setAddressDetail(address.getAddressDetail());
        dto.setDistrict(address.getDistrict());
        dto.setWard(address.getWard());
        dto.setProvince(address.getProvince());
        dto.setLatitude(address.getLatitude());
        dto.setLongitude(address.getLongitude());
        dto.setIsDefault(address.getIsDefault());
        dto.setUserId(address.getUser().getId());
        return dto;
    }
    public static Address transferToEntity(AddressDTO dto) {
        Address address = new Address();
        address.setId(dto.getId());
        address.setName(dto.getName());
        address.setAddressDetail(dto.getAddressDetail());
        address.setDistrict(dto.getDistrict());
        address.setWard(dto.getWard());
        address.setProvince(dto.getProvince());
        address.setLatitude(dto.getLatitude());
        address.setLongitude(dto.getLongitude());
        address.setIsDefault(dto.getIsDefault());
        UserService userService = new UserService();
        address.setUser(userService.findById(dto.getUserId()).orElseThrow(()-> new IllegalStateException("User not found")));
        return address;
    }
}