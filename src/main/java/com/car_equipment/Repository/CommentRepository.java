package com.car_equipment.Repository;

import com.car_equipment.Model.Address;
import com.car_equipment.Model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findByUserId(String userId);
    List<Comment> findByProductId(String productId);
}