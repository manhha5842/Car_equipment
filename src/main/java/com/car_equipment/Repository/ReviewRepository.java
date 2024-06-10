package com.car_equipment.Repository;

import com.car_equipment.Model.Product;
import com.car_equipment.Model.Review;
import com.car_equipment.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
    List<Review> findByProduct(Product product);
    List<Review> findByUser(User user);
}