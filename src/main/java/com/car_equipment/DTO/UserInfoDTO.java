package com.car_equipment.DTO;


import com.car_equipment.Model.Address;
import lombok.Data;

import java.util.Set;

@Data
public class UserInfoDTO {
    private String id;
    private String email;
    private String fullName;
    private Set<AddressDTO> addresses;
    private String phoneNumber;
    private String avatar;
    private String role;

    public UserInfoDTO(String id, String email, String fullName, Set<AddressDTO> addresses, String phoneNumber, String avatar, String role) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.addresses = addresses;
        this.phoneNumber = phoneNumber;
        this.avatar = avatar;
        this.role = role;
    }
}

