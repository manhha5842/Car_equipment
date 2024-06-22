package com.car_equipment.DTO;

import lombok.Data;

@Data
public class CommentDTO {
    private String id;
    private String productId;
    private String userId;
    private String message;

}