package com.car_equipment.DTO;

import com.car_equipment.Model.Reply;
import lombok.Data;

import java.sql.Time;

@Data
public class ReplyDTO {
    private String id;
    private String reviewId;
    private String userId;
    private String message;
    private Time createdAt;

    public static ReplyDTO transferToDTO(Reply reply) {
        ReplyDTO dto = new ReplyDTO();
        dto.setId(reply.getId());
        dto.setReviewId(reply.getReview().getId());
        dto.setUserId(reply.getUser().getId());
        dto.setMessage(reply.getMessage());
        dto.setCreatedAt(reply.getCreatedAt());
        return dto;
    }
}