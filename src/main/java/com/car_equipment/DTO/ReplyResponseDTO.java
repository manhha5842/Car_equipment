package com.car_equipment.DTO;

import com.car_equipment.Model.Reply;
import lombok.Data;

@Data
public class ReplyResponseDTO {
    private String id;
    private String userId;
    private String commentId;
    private String name;
    private String image;
    private String message;
    private String createdAt;

    public static ReplyResponseDTO transferToDTO(Reply reply) {
        ReplyResponseDTO dto = new ReplyResponseDTO();
        dto.setId(reply.getId());
        dto.setCommentId(reply.getComment().getId());
        dto.setImage(reply.getUser().getAvatar());
        dto.setName(reply.getUser().getFullName());
        dto.setMessage(reply.getMessage());
        dto.setCreatedAt(String.valueOf(reply.getCreatedAt()));
        return dto;

    }
}