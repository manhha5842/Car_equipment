package com.car_equipment.Service;

import com.car_equipment.DTO.AddressDTO;
import com.car_equipment.DTO.OrderDTO;
import com.car_equipment.Model.Address;
import com.car_equipment.Model.EnumOrderStatus;
import com.car_equipment.Model.Order;
import com.car_equipment.Model.User;
import com.car_equipment.Repository.AddressRepository;
import com.car_equipment.Repository.OrderRepository;
import com.car_equipment.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserService userService;

    // Lấy tất cả các order
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(OrderDTO::transferToDTO).collect(Collectors.toList());
    }

    // Lấy tất cả các order theo trang (pagination)
    public Page<OrderDTO> getAllOrders(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(OrderDTO::transferToDTO);
    }

    // Lấy order theo user
    public List<OrderDTO> getOrdersByUser(String userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(OrderDTO::transferToDTO).collect(Collectors.toList());
    }

    // Lấy order theo user và trang
    public Page<OrderDTO> getOrdersByUser(String userId, Pageable pageable) {
        Page<Order> orders = orderRepository.findByUserId(userId, pageable);
        return orders.map(OrderDTO::transferToDTO);
    }

    // Lấy order theo status
    public List<OrderDTO> getOrdersByStatus(EnumOrderStatus status) {
        List<Order> orders = orderRepository.findByStatus(status);
        return orders.stream().map(OrderDTO::transferToDTO).collect(Collectors.toList());
    }

    // Lấy order theo status và trang
    public Page<OrderDTO> getOrdersByStatus(EnumOrderStatus status, Pageable pageable) {
        Page<Order> orders = orderRepository.findByStatus(status, pageable);
        return orders.map(OrderDTO::transferToDTO);
    }

    // Xem chi tiết Order
    public OrderDTO getOrderById(String id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(OrderDTO::transferToDTO).orElse(null);
    }

    // Thêm Order
    public OrderDTO addOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setOrderDateTime(orderDTO.getOrderDateTime());
        order.setDeliveryFee(orderDTO.getDeliveryFee());
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setPaid(orderDTO.isPaid());
        order.setStatus(EnumOrderStatus.valueOf(orderDTO.getStatus()));
        order.setReview(orderDTO.getReview());
        order.setNote(orderDTO.getNote());

        Optional<User> userOptional = userRepository.findById(orderDTO.getUserId());
        userOptional.ifPresent(order::setUser);

        Address address = transferToEntity(orderDTO.getAddress());
        addressRepository.save(address);
        order.setAddress(address);

        Order savedOrder = orderRepository.save(order);
        return OrderDTO.transferToDTO(savedOrder);
    }
    public Address transferToEntity(AddressDTO dto) {
        Address address = new Address();
        address.setId(dto.getId());
        address.setName(dto.getName());
        address.setAddressDetail(dto.getAddressDetail());
        address.setDistrict(dto.getDistrict());
        address.setWard(dto.getWard());
        address.setProvince(dto.getProvince());
        address.setLatitude(dto.getLatitude());
        address.setLongitude(dto.getLongitude());
        address.setIsDefault(dto.getIsDefault());
        Optional<User> userOptional = userRepository.findById(dto.getUserId());
        userOptional.ifPresent(address::setUser);
        return address;
    }
    // Sửa Order
    public OrderDTO updateOrder(String id, OrderDTO orderDTO) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setOrderDateTime(orderDTO.getOrderDateTime());
            order.setDeliveryFee(orderDTO.getDeliveryFee());
            order.setTotalAmount(orderDTO.getTotalAmount());
            order.setPaid(orderDTO.isPaid());
            order.setStatus(EnumOrderStatus.valueOf(orderDTO.getStatus()));
            order.setReview(orderDTO.getReview());
            order.setNote(orderDTO.getNote());

            Optional<User> userOptional = userRepository.findById(orderDTO.getUserId());
            userOptional.ifPresent(order::setUser);

            Address address = transferToEntity(orderDTO.getAddress());
            addressRepository.save(address);
            order.setAddress(address);

            Order updatedOrder = orderRepository.save(order);
            return OrderDTO.transferToDTO(updatedOrder);
        }
        return null;
    }

    // Xoá Order
    public boolean deleteOrder(String id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Cập nhật trạng thái Order
    public OrderDTO updateOrderStatus(String id, EnumOrderStatus status) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(status);
            Order updatedOrder = orderRepository.save(order);
            return OrderDTO.transferToDTO(updatedOrder);
        }
        return null;
    }
}