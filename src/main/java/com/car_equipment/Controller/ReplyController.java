package com.car_equipment.Controller;

import com.car_equipment.DTO.ReplyDTO;
import com.car_equipment.DTO.ReplyResponseDTO;
import com.car_equipment.Service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/replies")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    // Lấy danh sách Reply
    @GetMapping
    public ResponseEntity<?> getAllReplies() {
        List<ReplyResponseDTO> replies = replyService.getAllReplies();
        return ResponseEntity.ok(replies);
    }

    // Xem chi tiết Reply
    @GetMapping("/{id}")
    public ResponseEntity<?> getReplyById(@PathVariable String id) {
        ReplyResponseDTO reply = replyService.getReplyById(id);
        if (reply != null) {
            return ResponseEntity.ok(reply);
        }
        return ResponseEntity.notFound().build();
    }

    // Thêm Reply
    @PostMapping
    public ResponseEntity<?> addReply(@RequestBody ReplyDTO replyDTO) {
        ReplyResponseDTO newReply = replyService.addReply(replyDTO);
        return ResponseEntity.ok(newReply);
    }

    // Sửa Reply
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReply(@RequestBody ReplyDTO replyDTO) {
        ReplyResponseDTO updatedReply = replyService.updateReply(replyDTO);
        if (updatedReply != null) {
            return ResponseEntity.ok(updatedReply);
        }
        return ResponseEntity.notFound().build();
    }

    // Xoá Reply
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReply(@PathVariable String id) {
        boolean isDeleted = replyService.deleteReply(id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}