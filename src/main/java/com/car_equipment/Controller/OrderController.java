package com.car_equipment.Controller;

import com.car_equipment.DTO.OrderDTO;
import com.car_equipment.DTO.OrderInputDTO;
import com.car_equipment.Model.EnumOrderStatus;
import com.car_equipment.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Lấy tất cả các order
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Lấy tất cả các order theo trang (pagination)
    @GetMapping("/page")
    public ResponseEntity<Page<OrderDTO>> getAllOrders(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDTO> orders = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(orders);
    }

    // Lấy order theo user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable String userId) {
        List<OrderDTO> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }

    // Lấy order theo user và trang
    @GetMapping("/user/{userId}/page")
    public ResponseEntity<Page<OrderDTO>> getOrdersByUser(@PathVariable String userId, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDTO> orders = orderService.getOrdersByUser(userId, pageable);
        return ResponseEntity.ok(orders);
    }

    // Lấy order theo status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@PathVariable String status) {
        System.out.println(status);
        List<OrderDTO> orders = orderService.getOrdersByStatus(EnumOrderStatus.valueOf(status));
        return ResponseEntity.ok(orders);
    }

    // Lấy order theo status và trang
    @GetMapping("/status/{status}/page")
    public ResponseEntity<Page<OrderDTO>> getOrdersByStatus(@PathVariable EnumOrderStatus status, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDTO> orders = orderService.getOrdersByStatus(status, pageable);
        return ResponseEntity.ok(orders);
    }

    // Xem chi tiết Order
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable String id) {
        OrderDTO order = orderService.getOrderById(id);
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }

    // Thêm Order
    @PostMapping
    public ResponseEntity<OrderDTO> addOrder(@RequestBody OrderInputDTO orderInputDTO) {
        OrderDTO newOrder = orderService.addOrder(orderInputDTO);
        return ResponseEntity.ok(newOrder);
    }

    // Sửa Order
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable String id, @RequestBody OrderDTO orderDTO) {
        OrderDTO updatedOrder = orderService.updateOrder(id, orderDTO);
        if (updatedOrder != null) {
            return ResponseEntity.ok(updatedOrder);
        }
        return ResponseEntity.notFound().build();
    }

    // Xoá Order
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String id) {
        boolean isDeleted = orderService.deleteOrder(id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Cập nhật trạng thái Order
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable String id, @RequestParam EnumOrderStatus status) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(id, status);
        if (updatedOrder != null) {
            return ResponseEntity.ok(updatedOrder);
        }
        return ResponseEntity.notFound().build();
    }
}