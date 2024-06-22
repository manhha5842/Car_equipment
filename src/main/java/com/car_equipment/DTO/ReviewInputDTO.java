package com.car_equipment.DTO;

import com.car_equipment.Model.Review;
import lombok.Data;

@Data
public class ReviewInputDTO {
    private String id;
    private String productId;
    private String userId;
    private int rate;
    private String message ;

    public static ReviewInputDTO transferToDTO(Review review) {
        ReviewInputDTO dto = new ReviewInputDTO();
        dto.setId(review.getId());
        dto.setProductId(review.getProduct().getId());
        dto.setUserId(review.getUser().getId());
        dto.setRate(review.getRate());
        dto.setMessage(review.getMessage());
        return dto;
    }
}