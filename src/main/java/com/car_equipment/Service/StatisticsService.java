package com.car_equipment.Service;

import com.car_equipment.DTO.CategoryRevenueStatisticsDTO;
import com.car_equipment.DTO.RevenueStatisticsDTO;
import com.car_equipment.Model.Category;
import com.car_equipment.Model.Order;
import com.car_equipment.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    @Autowired
    private OrderRepository orderRepository;

    public List<RevenueStatisticsDTO> getRevenueStatistics(Timestamp startTime, Timestamp endTime) {
        List<Order> orders = orderRepository.findAllByOrderDateTimeBetween(startTime, endTime);
        return orders.stream()
                .collect(Collectors.groupingBy(order -> order.getOrderDateTime().toLocalDateTime().toLocalDate()))
                .entrySet().stream()
                .map(entry -> {
                    RevenueStatisticsDTO dto = new RevenueStatisticsDTO();
                    dto.setPeriod(entry.getKey().toString());
                    dto.setRevenue(entry.getValue().stream().mapToInt(Order::getTotalAmount).sum());
                    return dto;
                })
                .collect(Collectors.toList());
    }


    public List<CategoryRevenueStatisticsDTO> getCategoryRevenueStatistics(int month, int year) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startTime = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endTime = yearMonth.atEndOfMonth().atTime(23, 59, 59);
        Timestamp startTimestamp = Timestamp.valueOf(startTime);
        Timestamp endTimestamp = Timestamp.valueOf(endTime);

        List<Order> orders = orderRepository.findAllByOrderDateTimeBetween(startTimestamp, endTimestamp);
        return orders.stream()
                .flatMap(order -> order.getOrderProducts().stream())
                .collect(Collectors.groupingBy(orderProduct -> orderProduct.getProduct().getCategories().stream()
                        .map(Category::getName)
                        .collect(Collectors.joining(", "))))
                .entrySet().stream()
                .map(entry -> {
                    CategoryRevenueStatisticsDTO dto = new CategoryRevenueStatisticsDTO();
                    dto.setCategory(entry.getKey());
                    dto.setRevenue(entry.getValue().stream().mapToInt(orderProduct -> orderProduct.getQuantity() * orderProduct.getProduct().getPrice()).sum());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}