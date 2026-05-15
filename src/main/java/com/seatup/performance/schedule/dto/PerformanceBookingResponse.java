package com.seatup.performance.schedule.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerformanceBookingResponse {

    private Long performanceId;

    private List<ScheduleDetailResponse> schedules;

    public PerformanceBookingResponse(Long performanceId, List<ScheduleDetailResponse> schedules) {
        this.performanceId = performanceId;
        this.schedules = schedules;
    }
}
