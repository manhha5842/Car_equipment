package com.car_equipment.DTO;

import lombok.Data;

@Data
public class ReplyDTO {
    private String id;
    private String commentId;
    private String userId;
    private String message;

}