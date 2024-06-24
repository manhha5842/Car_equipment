package com.car_equipment.Controller;

import com.car_equipment.DTO.CategoryRevenueStatisticsDTO;
import com.car_equipment.DTO.RevenueStatisticsDTO;
import com.car_equipment.Service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/revenue")
    public ResponseEntity<List<RevenueStatisticsDTO>> getRevenueStatistics(
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endTime) {

        LocalDateTime startDateTime = startTime.atStartOfDay();
        LocalDateTime endDateTime = endTime.atTime(23, 59, 59);
        Timestamp startTimestamp = Timestamp.valueOf(startDateTime);
        Timestamp endTimestamp = Timestamp.valueOf(endDateTime);

        List<RevenueStatisticsDTO> statistics = statisticsService.getRevenueStatistics(startTimestamp, endTimestamp);
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/revenue-week")
    public ResponseEntity<List<RevenueStatisticsDTO>> getRevenueStatisticsWeek(
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endTime) {

        LocalDateTime startDateTime = startTime.atStartOfDay();
        LocalDateTime endDateTime = endTime.atTime(23, 59, 59);
        Timestamp startTimestamp = Timestamp.valueOf(startDateTime);
        Timestamp endTimestamp = Timestamp.valueOf(endDateTime);

        List<RevenueStatisticsDTO> statistics = statisticsService.getRevenueStatisticsWeek(startTimestamp, endTimestamp);
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/revenue-month")
    public ResponseEntity<List<RevenueStatisticsDTO>> getRevenueStatisticsMonth(
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endTime) {

        LocalDateTime startDateTime = startTime.atStartOfDay();
        LocalDateTime endDateTime = endTime.atTime(23, 59, 59);
        Timestamp startTimestamp = Timestamp.valueOf(startDateTime);
        Timestamp endTimestamp = Timestamp.valueOf(endDateTime);

        List<RevenueStatisticsDTO> statistics = statisticsService.getRevenueStatisticsMonth(startTimestamp, endTimestamp);
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/revenue-auto")
    public ResponseEntity<?> getRevenueStatisticsAuto(
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endTime) {

        LocalDateTime startDateTime = startTime.atStartOfDay();
        LocalDateTime endDateTime = endTime.atTime(23, 59, 59);
        Timestamp startTimestamp = Timestamp.valueOf(startDateTime);
        Timestamp endTimestamp = Timestamp.valueOf(endDateTime);
        if (endTime.minusDays(8).isAfter(startTime)) {
            List<RevenueStatisticsDTO> statistics = statisticsService.getRevenueStatisticsWeek(startTimestamp, endTimestamp);
            return ResponseEntity.ok(statistics);
        } else if (endTime.minusDays(32).isAfter(startTime)) {
            List<RevenueStatisticsDTO> statistics = statisticsService.getRevenueStatisticsMonth(startTimestamp, endTimestamp);
            return ResponseEntity.ok(statistics);

        }else{
            List<RevenueStatisticsDTO> statistics = statisticsService.getRevenueStatistics(startTimestamp, endTimestamp);
            return ResponseEntity.ok(statistics);
        }

    }

    @GetMapping("/category-revenue")
    public ResponseEntity<List<CategoryRevenueStatisticsDTO>> getCategoryRevenueStatistics(
            @RequestParam("month") int month,
            @RequestParam("year") int year) {

        List<CategoryRevenueStatisticsDTO> statistics = statisticsService.getCategoryRevenueStatistics(month, year);
        return ResponseEntity.ok(statistics);
    }
}