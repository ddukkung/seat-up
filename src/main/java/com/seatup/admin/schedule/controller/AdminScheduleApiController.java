package com.seatup.admin.schedule.controller;

import com.seatup.admin.schedule.dto.AdminScheduleListResponse;
import com.seatup.admin.schedule.dto.AdminScheduleResponse;
import com.seatup.admin.schedule.dto.RegisterScheduleRequest;
import com.seatup.admin.schedule.dto.UpdateScheduleRequest;
import com.seatup.admin.schedule.service.AdminPerformScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/schedules")
public class AdminScheduleApiController {

    private final AdminPerformScheduleService scheduleService;

    public AdminScheduleApiController(AdminPerformScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public ResponseEntity<AdminScheduleListResponse> getSchedules(@RequestParam("performanceId") Long performanceId) {
        AdminScheduleListResponse schedules = scheduleService.findSchedules(performanceId);
        return ResponseEntity.ok(schedules);
    }

    @PostMapping
    public ResponseEntity<String> saveSchedule(@RequestBody RegisterScheduleRequest request) {
        String performanceStatus = scheduleService.save(request);
        return ResponseEntity.ok(performanceStatus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminScheduleResponse> getSchedule(@PathVariable("id") Long id) {
        AdminScheduleResponse schedule = scheduleService.findSchedule(id);
        return ResponseEntity.ok(schedule);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateSchedule(@PathVariable("id") Long id, @RequestBody UpdateScheduleRequest request) {
        scheduleService.updateSchedule(id, request);
        return ResponseEntity.ok("회차가 수정되었습니다.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSchedule(@PathVariable("id") Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.ok("회차가 삭제되었습니다.");
    }

}
