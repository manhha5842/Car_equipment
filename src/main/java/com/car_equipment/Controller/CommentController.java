package com.car_equipment.Controller;

import com.car_equipment.DTO.CommentDTO;
import com.car_equipment.DTO.CommentResponseDTO;
import com.car_equipment.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // Lấy danh sách comment
    @GetMapping
    public ResponseEntity<List<CommentResponseDTO>> getAllComments() {
        List<CommentResponseDTO> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    // Lấy danh sách comment theo productId
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<CommentResponseDTO>> getAllCommentsByProductId(@PathVariable String productId) {
        List<CommentResponseDTO> comments = commentService.getAllCommentsByProductId(productId);
        return ResponseEntity.ok(comments);
    }

    // Xem chi tiết comment
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> getCommentById(@PathVariable String id) {
        CommentResponseDTO comment = commentService.getCommentById(id);
        if (comment != null) {
            return ResponseEntity.ok(comment);
        }
        return ResponseEntity.notFound().build();
    }

    // Thêm comment
    @PostMapping
    public ResponseEntity<CommentResponseDTO> addComment(@RequestBody CommentDTO commentDTO) {
        CommentResponseDTO newComment = commentService.addComment(commentDTO);
        if (newComment != null) {
            return ResponseEntity.ok(newComment);
        }
        return ResponseEntity.badRequest().build();
    }

    // Sửa comment
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> updateComment(@PathVariable String id, @RequestBody CommentDTO commentDTO) {
        commentDTO.setId(id);
        CommentResponseDTO updatedComment = commentService.updateComment(commentDTO);
        if (updatedComment != null) {
            return ResponseEntity.ok(updatedComment);
        }
        return ResponseEntity.notFound().build();
    }

    // Xoá comment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable String id) {
        boolean isDeleted = commentService.deleteComment(id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}