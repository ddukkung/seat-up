package com.seatup.admin.schedule.dto;

import com.seatup.performance.schedule.entity.PerformanceSchedule;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleSummaryResponse {

    private Long id;

    private Long performanceId;

    private LocalDate performanceDate;

    private LocalTime performanceTime;

    private int round;

    public ScheduleSummaryResponse(Long id, Long performanceId, LocalDate performanceDate, LocalTime performanceTime, int round) {
        this.id = id;
        this.performanceId = performanceId;
        this.performanceDate = performanceDate;
        this.performanceTime = performanceTime;
        this.round = round;
    }

    public static ScheduleSummaryResponse from(PerformanceSchedule entity) {
        return new ScheduleSummaryResponse(
                entity.getId(),
                entity.getPerformance().getId(),
                entity.getPerformanceDate(),
                entity.getPerformanceTime(),
                entity.getRound()
        );
    }

}
