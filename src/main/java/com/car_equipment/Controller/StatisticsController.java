package com.car_equipment.Controller;

import com.car_equipment.DTO.CategoryRevenueStatisticsDTO;
import com.car_equipment.DTO.RevenueStatisticsDTO;
import com.car_equipment.Service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/revenue")
    public ResponseEntity<List<RevenueStatisticsDTO>> getRevenueStatistics(
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        Timestamp startTimestamp = Timestamp.valueOf(startTime);
        Timestamp endTimestamp = Timestamp.valueOf(endTime);

        List<RevenueStatisticsDTO> statistics = statisticsService.getRevenueStatistics(startTimestamp, endTimestamp);
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/category-revenue")
    public ResponseEntity<List<CategoryRevenueStatisticsDTO>> getCategoryRevenueStatistics(
            @RequestParam("month") int month,
            @RequestParam("year") int year) {

        List<CategoryRevenueStatisticsDTO> statistics = statisticsService.getCategoryRevenueStatistics(month, year);
        return ResponseEntity.ok(statistics);
    }
}