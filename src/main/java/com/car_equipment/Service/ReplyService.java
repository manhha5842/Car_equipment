package com.car_equipment.Service;

import com.car_equipment.DTO.ReplyDTO;
import com.car_equipment.Model.Reply;
import com.car_equipment.Model.Review;
import com.car_equipment.Model.User;
import com.car_equipment.Repository.ReplyRepository;
import com.car_equipment.Repository.ReviewRepository;
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
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    // Lấy danh sách Reply
    public List<ReplyDTO> getAllReplies() {
        List<Reply> replies = replyRepository.findAll();
        return replies.stream().map(ReplyDTO::transferToDTO).collect(Collectors.toList());
    }

    // Xem chi tiết Reply
    public ReplyDTO getReplyById(String id) {
        Optional<Reply> reply = replyRepository.findById(id);
        return reply.map(ReplyDTO::transferToDTO).orElse(null);
    }

    // Thêm Reply
    public ReplyDTO addReply(ReplyDTO replyDTO) {
        Reply reply = new Reply();
        reply.setMessage(replyDTO.getMessage());
        reply.setCreatedAt(replyDTO.getCreatedAt());

        Optional<Review> reviewOptional = reviewRepository.findById(replyDTO.getReviewId());
        reviewOptional.ifPresent(reply::setReview);

        Optional<User> userOptional = userRepository.findById(replyDTO.getUserId());
        userOptional.ifPresent(reply::setUser);

        Reply savedReply = replyRepository.save(reply);
        return ReplyDTO.transferToDTO(savedReply);
    }

    // Sửa Reply
    public ReplyDTO updateReply(String id, ReplyDTO replyDTO) {
        Optional<Reply> replyOptional = replyRepository.findById(id);
        if (replyOptional.isPresent()) {
            Reply reply = replyOptional.get();
            reply.setMessage(replyDTO.getMessage());
            reply.setCreatedAt(replyDTO.getCreatedAt());

            Optional<Review> reviewOptional = reviewRepository.findById(replyDTO.getReviewId());
            reviewOptional.ifPresent(reply::setReview);

            Optional<User> userOptional = userRepository.findById(replyDTO.getUserId());
            userOptional.ifPresent(reply::setUser);

            Reply updatedReply = replyRepository.save(reply);
            return ReplyDTO.transferToDTO(updatedReply);
        }
        return null;
    }

    // Xoá Reply
    public boolean deleteReply(String id) {
        Optional<Reply> replyOptional = replyRepository.findById(id);
        if (replyOptional.isPresent()) {
            replyRepository.deleteById(id);
            return true;
        }
        return false;
    }
}