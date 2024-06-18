package com.car_equipment.DTO;

import com.car_equipment.Model.Order;
import com.car_equipment.Model.OrderStatus;
import com.car_equipment.Model.Product;
import lombok.Data;

import java.sql.Time;
import java.util.Set;
import java.util.stream.Collectors;

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
    private Set<ProductDTO> products;

    public static OrderDTO transferToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setUserId(order.getUser().getId());
        orderDTO.setAddressId(order.getAddress().getId());
        orderDTO.setOrderDateTime(order.getOrderDateTime());
        orderDTO.setDeliveryFee(order.getDeliveryFee());
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setPaid(order.isPaid());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setReview(order.getReview());
        orderDTO.setNote(order.getNote());
        orderDTO.setProducts(order.getProducts().stream().map(ProductDTO::transferToDTO).collect(Collectors.toSet()));
        return orderDTO;
    }
}