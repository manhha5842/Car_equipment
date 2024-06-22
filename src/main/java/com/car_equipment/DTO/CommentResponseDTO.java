package com.car_equipment.DTO;

import com.car_equipment.Model.Comment;
import com.car_equipment.Model.Reply;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CommentResponseDTO {
    private String id;
    private String userId;
    private String name;
    private String image;
    private String message;
    List<ReplyResponseDTO> replies;
    private String createdAt;

    public static CommentResponseDTO transferToDTO(Comment comment) {
        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setId(comment.getId());
        dto.setUserId(comment.getUser().getId());
        dto.setImage(comment.getUser().getAvatar());
        dto.setName(comment.getUser().getFullName());
        dto.setMessage(comment.getMessage());
        dto.setCreatedAt(String.valueOf(comment.getCreatedAt()));

        if (comment.getReplies() != null) {
            List<ReplyResponseDTO> replyResponseDTOS = new ArrayList<>();
            for (Reply reply : comment.getReplies()) {
                replyResponseDTOS.add(ReplyResponseDTO.transferToDTO(reply));
            }
            dto.setReplies(replyResponseDTOS);
        }

        return dto;

    }
}