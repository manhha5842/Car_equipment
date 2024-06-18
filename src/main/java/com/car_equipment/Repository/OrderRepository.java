package com.car_equipment.Repository;

import com.car_equipment.Model.Order;
import com.car_equipment.Model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    Page<Order> findAll(Pageable pageable);
    List<Order> findByUserId(String userId);
    Page<Order> findByUserId(String userId, Pageable pageable);
    List<Order> findByStatus(OrderStatus status);
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
}