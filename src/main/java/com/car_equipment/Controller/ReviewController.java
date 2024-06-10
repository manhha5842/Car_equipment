package com.car_equipment.Controller;

import com.car_equipment.DTO.ReviewDTO;
import com.car_equipment.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // Lấy danh sách Review
    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        List<ReviewDTO> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    // Xem chi tiết Review
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable String id) {
        ReviewDTO review = reviewService.getReviewById(id);
        if (review != null) {
            return ResponseEntity.ok(review);
        }
        return ResponseEntity.notFound().build();
    }

    // Thêm Review
    @PostMapping
    public ResponseEntity<ReviewDTO> addReview(@RequestBody ReviewDTO reviewDTO) {
        ReviewDTO newReview = reviewService.addReview(reviewDTO);
        return ResponseEntity.ok(newReview);
    }

    // Sửa Review
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable String id, @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO updatedReview = reviewService.updateReview(id, reviewDTO);
        if (updatedReview != null) {
            return ResponseEntity.ok(updatedReview);
        }
        return ResponseEntity.notFound().build();
    }

    // Xoá Review
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable String id) {
        boolean isDeleted = reviewService.deleteReview(id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Lấy review theo product
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByProduct(@PathVariable String productId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByProduct(productId);
        return ResponseEntity.ok(reviews);
    }

    // Lấy review theo user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByUser(@PathVariable String userId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByUser(userId);
        return ResponseEntity.ok(reviews);
    }
}