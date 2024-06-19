package com.car_equipment.DTO;

import lombok.Data;

@Data
public class ProductCartDTO {
    private ProductDTO product;
    private int quantity;
}