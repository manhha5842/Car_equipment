package com.car_equipment.DTO;

import com.car_equipment.Model.Order;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class OrderInputDTO {
    private String id;
    private String userId;
    private String addressId;
    private int deliveryFee;
    private int totalAmount;
    private boolean isPaid;
    private String note;
    private Set<ProductCartInputDTO> products;


}