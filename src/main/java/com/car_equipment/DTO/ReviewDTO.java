package com.car_equipment.DTO;

import com.car_equipment.Model.Review;
import lombok.Data;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ReviewDTO {
    private String id;
    private String productId;
    private String userId;
    private int rate;
    private String message;
    private String createdAt;
    private List<ReplyDTO> replies;

    public static ReviewDTO transferToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setProductId(review.getProduct().getId());
        dto.setUserId(review.getUser().getId());
        dto.setRate(review.getRate());
        dto.setMessage(review.getMessage());
        dto.setCreatedAt(review.getCreatedAt().toString());
        dto.setReplies(review.getReplies().stream().map(ReplyDTO::transferToDTO).collect(Collectors.toList()));
        return dto;
    }
}