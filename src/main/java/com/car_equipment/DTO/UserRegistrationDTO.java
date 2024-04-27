package com.car_equipment.DTO;

import lombok.Data;

@Data
public class UserRegistrationDTO {
    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
}
