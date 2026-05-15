package com.seatup.performance.schedule.controller;

import com.seatup.performance.schedule.dto.PerformanceBookingResponse;
import com.seatup.performance.schedule.service.PerformanceScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedules")
public class PerformanceScheduleApiController {

    private final PerformanceScheduleService scheduleService;

    public PerformanceScheduleApiController(PerformanceScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/{performanceId}")
    public ResponseEntity<PerformanceBookingResponse> getSchedules(@PathVariable("performanceId") Long performanceId) {
        PerformanceBookingResponse schedules = scheduleService.getSchedules(performanceId);
        return ResponseEntity.ok(schedules);
    }
}
