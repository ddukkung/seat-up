package com.seatup.performance.schedule.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PerformanceBookingResponse {
    private Long performanceId;
    private List<ScheduleDetailResponse> schedules;
}
