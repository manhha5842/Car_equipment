package com.car_equipment.DTO;

import com.car_equipment.Model.Order;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class OrderDTO {
    private String id;
    private String userId;
    private AddressDTO address;
    private Timestamp orderDateTime;
    private int deliveryFee;
    private int totalAmount;
    private boolean isPaid;
    private String status;
    private String review;
    private String note;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Set<ProductCartDTO> products;

    public static OrderDTO transferToDTO(Order order) {

        System.out.println("haha");
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setAddress(AddressDTO.transferToDTO(order.getAddress()));
        dto.setOrderDateTime(order.getOrderDateTime());
        dto.setDeliveryFee(order.getDeliveryFee());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setPaid(order.isPaid());
        dto.setStatus(order.getStatus().name());
        dto.setReview(order.getReview());
        dto.setNote(order.getNote());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());

        System.out.println("hihi");
        dto.setProducts(order.getOrderProducts().stream().map(orderProduct -> {
            ProductCartDTO productCartDTO = new ProductCartDTO();
            productCartDTO.setProduct(ProductDTO.transferToDTO(orderProduct.getProduct()));
            productCartDTO.setQuantity(orderProduct.getQuantity());
            return productCartDTO;
        }).collect(Collectors.toSet()));
        return dto;
    }
}