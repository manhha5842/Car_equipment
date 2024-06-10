package com.car_equipment.Service;

import com.car_equipment.DTO.ReviewDTO;
import com.car_equipment.Model.Product;
import com.car_equipment.Model.Review;
import com.car_equipment.Model.User;
import com.car_equipment.Repository.ProductRepository;
import com.car_equipment.Repository.ReviewRepository;
import com.car_equipment.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // Lấy danh sách Review
    public List<ReviewDTO> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream().map(ReviewDTO::transferToDTO).collect(Collectors.toList());
    }

    // Xem chi tiết Review
    public ReviewDTO getReviewById(String id) {
        Optional<Review> review = reviewRepository.findById(id);
        return review.map(ReviewDTO::transferToDTO).orElse(null);
    }

    // Thêm Review
    public ReviewDTO addReview(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setRate(reviewDTO.getRate());
        review.setMessage(reviewDTO.getMessage());
        review.setCreatedAt(reviewDTO.getCreatedAt());

        Optional<Product> productOptional = productRepository.findById(reviewDTO.getProductId());
        productOptional.ifPresent(review::setProduct);

        Optional<User> userOptional = userRepository.findById(reviewDTO.getUserId());
        userOptional.ifPresent(review::setUser);

        Review savedReview = reviewRepository.save(review);
        return ReviewDTO.transferToDTO(savedReview);
    }

    // Sửa Review
    public ReviewDTO updateReview(String id, ReviewDTO reviewDTO) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (reviewOptional.isPresent()) {
            Review review = reviewOptional.get();
            review.setRate(reviewDTO.getRate());
            review.setMessage(reviewDTO.getMessage());
            review.setCreatedAt(reviewDTO.getCreatedAt());

            Optional<Product> productOptional = productRepository.findById(reviewDTO.getProductId());
            productOptional.ifPresent(review::setProduct);

            Optional<User> userOptional = userRepository.findById(reviewDTO.getUserId());
            userOptional.ifPresent(review::setUser);

            Review updatedReview = reviewRepository.save(review);
            return ReviewDTO.transferToDTO(updatedReview);
        }
        return null;
    }

    // Xoá Review
    public boolean deleteReview(String id) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (reviewOptional.isPresent()) {
            reviewRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Lấy review theo product
    public List<ReviewDTO> getReviewsByProduct(String productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            List<Review> reviews = reviewRepository.findByProduct(product);
            return reviews.stream().map(ReviewDTO::transferToDTO).collect(Collectors.toList());
        }
        return null;
    }

    // Lấy review theo user
    public List<ReviewDTO> getReviewsByUser(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Review> reviews = reviewRepository.findByUser(user);
            return reviews.stream().map(ReviewDTO::transferToDTO).collect(Collectors.toList());
        }
        return null;
    }
}