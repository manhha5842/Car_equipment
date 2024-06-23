package com.car_equipment.DTO;

import lombok.Data;

@Data
public class ProductCartInputDTO {
    private String productId;
    private int quantity;
}