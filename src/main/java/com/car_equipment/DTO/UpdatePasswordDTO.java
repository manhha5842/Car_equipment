package com.car_equipment.DTO;

import lombok.Data;

@Data
public class UpdatePasswordDTO {
    private String currentPassword;
    private String newPassword;
}