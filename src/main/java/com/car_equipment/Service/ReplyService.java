package com.car_equipment.Service;

import com.car_equipment.DTO.ReplyDTO;
import com.car_equipment.DTO.ReplyResponseDTO;
import com.car_equipment.Model.Comment;
import com.car_equipment.Model.Reply;
import com.car_equipment.Model.User;
import com.car_equipment.Repository.CommentRepository;
import com.car_equipment.Repository.ReplyRepository;
import com.car_equipment.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReplyService {

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    // Lấy danh sách reply
    public List<ReplyResponseDTO> getAllReplies() {
        List<Reply> replies = replyRepository.findAll();
        return replies.stream().map(ReplyResponseDTO::transferToDTO).collect(Collectors.toList());
    }

    // Lấy danh sách reply bằng  comment id
    public List<ReplyResponseDTO> getAllRepliesByCommentId(String id) {
        List<Reply> replies = replyRepository.findByCommentId(id);
        return replies.stream().map(ReplyResponseDTO::transferToDTO).collect(Collectors.toList());
    }


    // Xem chi tiết reply
    public ReplyResponseDTO getReplyById(String id) {
        Optional<Reply> reply = replyRepository.findById(id);
        return reply.map(ReplyResponseDTO::transferToDTO).orElse(null);
    }

    // Thêm reply
    public ReplyResponseDTO addReply(ReplyDTO dto) {
        Optional<Comment> commentOptional = commentRepository.findById(dto.getCommentId());
        Optional<User> userOptional = userRepository.findById(dto.getUserId());
        if (commentOptional.isPresent() && userOptional.isPresent()) {
            Reply reply = new Reply();
            reply.setComment(commentOptional.get());
            reply.setUser(userOptional.get());
            reply.setMessage(dto.getMessage());
            return ReplyResponseDTO.transferToDTO(replyRepository.save(reply));
        }
        return null;
    }

    // Sửa reply
    public ReplyResponseDTO updateReply(ReplyDTO dto) {
        Optional<Reply> replyOptional = replyRepository.findById(dto.getId());
        if (replyOptional.isPresent()) {
            Reply reply = replyOptional.get();
            reply.setMessage(dto.getMessage());
            reply = replyRepository.save(reply);
            return ReplyResponseDTO.transferToDTO(reply);
        }
        return null;
    }

    // Xoá reply
    public boolean deleteReply(String id) {
        Optional<Reply> replyOptional = replyRepository.findById(id);
        if (replyOptional.isPresent()) {
            replyRepository.deleteById(id);
            return true;
        }
        return false;
    }
}