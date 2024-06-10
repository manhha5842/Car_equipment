package com.car_equipment.Controller;

import com.car_equipment.DTO.ReplyDTO;
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
    public ResponseEntity<List<ReplyDTO>> getAllReplies() {
        List<ReplyDTO> replies = replyService.getAllReplies();
        return ResponseEntity.ok(replies);
    }

    // Xem chi tiết Reply
    @GetMapping("/{id}")
    public ResponseEntity<ReplyDTO> getReplyById(@PathVariable String id) {
        ReplyDTO reply = replyService.getReplyById(id);
        if (reply != null) {
            return ResponseEntity.ok(reply);
        }
        return ResponseEntity.notFound().build();
    }

    // Thêm Reply
    @PostMapping
    public ResponseEntity<ReplyDTO> addReply(@RequestBody ReplyDTO replyDTO) {
        ReplyDTO newReply = replyService.addReply(replyDTO);
        return ResponseEntity.ok(newReply);
    }

    // Sửa Reply
    @PutMapping("/{id}")
    public ResponseEntity<ReplyDTO> updateReply(@PathVariable String id, @RequestBody ReplyDTO replyDTO) {
        ReplyDTO updatedReply = replyService.updateReply(id, replyDTO);
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