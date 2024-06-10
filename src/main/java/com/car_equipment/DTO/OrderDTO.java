package com.car_equipment.DTO;

import com.car_equipment.Model.Order;
import com.car_equipment.Model.OrderStatus;
import lombok.Data;

import java.sql.Time;

@Data
public class OrderDTO {
    private String id;
    private String userId;
    private String addressId;
    private Time orderDateTime;
    private int deliveryFee;
    private int totalAmount;
    private boolean isPaid;
    private OrderStatus status;
    private String review;
    private String note;

    public static OrderDTO transferToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setAddressId(order.getAddress().getId());
        dto.setOrderDateTime(order.getOrderDateTime());
        dto.setDeliveryFee(order.getDeliveryFee());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setPaid(order.isPaid());
        dto.setStatus(order.getStatus());
        dto.setReview(order.getReview());
        dto.setNote(order.getNote());
        return dto;
    }
}