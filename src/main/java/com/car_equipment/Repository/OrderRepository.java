package com.car_equipment.Repository;

import com.car_equipment.Model.Order;
import com.car_equipment.Model.EnumOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUserId(String userId);
    Page<Order> findByUserId(String userId, Pageable pageable);
    List<Order> findByStatus(EnumOrderStatus status);
    Page<Order> findByStatus(EnumOrderStatus status, Pageable pageable);

    List<Order> findAllByOrderDateTimeBetween(Timestamp startTime, Timestamp endTime);
}