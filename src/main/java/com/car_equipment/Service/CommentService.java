package com.car_equipment.Service;

import com.car_equipment.DTO.CommentDTO;
import com.car_equipment.DTO.CommentResponseDTO;
import com.car_equipment.Model.Comment;
import com.car_equipment.Model.Product;
import com.car_equipment.Model.Reply;
import com.car_equipment.Model.User;
import com.car_equipment.Repository.CommentRepository;
import com.car_equipment.Repository.ProductRepository;
import com.car_equipment.Repository.ReplyRepository;
import com.car_equipment.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReplyRepository replyRepository;

    // Lấy danh sách comment
    public List<CommentResponseDTO> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream().map(CommentResponseDTO::transferToDTO).collect(Collectors.toList());
    }


    public List<CommentResponseDTO> getAllCommentsByProductId(String productId) {
        List<Comment> comments = commentRepository.findByProductId(productId);
        return comments.stream().map(CommentResponseDTO::transferToDTO).collect(Collectors.toList());
    }

    // Xem chi tiết comment
    public CommentResponseDTO getCommentById(String id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.map(CommentResponseDTO::transferToDTO).orElse(null);
    }

    // Thêm comment
    public CommentResponseDTO addComment(CommentDTO commentDTO) {
        Optional<User> userOptional = userRepository.findById(commentDTO.getUserId());
        Optional<Product> productOptional = productRepository.findById(commentDTO.getProductId());
        if (userOptional.isPresent() && productOptional.isPresent()) {
            Comment comment = new Comment();
            comment.setProduct(productOptional.get());
            comment.setUser(userOptional.get());
            comment.setMessage(commentDTO.getMessage());
            return CommentResponseDTO.transferToDTO(commentRepository.save(comment));
        }
        return null;
    }

    // Sửa comment
    public CommentResponseDTO updateComment(CommentDTO commentDTO) {
        Optional<Comment> commentOptional = commentRepository.findById(commentDTO.getId());
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            comment.setMessage(comment.getMessage());
            comment = commentRepository.save(comment);
            return CommentResponseDTO.transferToDTO(comment);
        }
        return null;
    }

    // Xoá comment
    public boolean deleteComment(String id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (commentOptional.isPresent()) {
            if(!commentOptional.get().getReplies().isEmpty()) {
                for(Reply reply : commentOptional.get().getReplies()) {
                    replyRepository.deleteById(reply.getId());
                }
            }
            commentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}