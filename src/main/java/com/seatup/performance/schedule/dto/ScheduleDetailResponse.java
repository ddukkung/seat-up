package com.seatup.performance.schedule.dto;

import com.seatup.performance.schedule.entity.PerformanceSchedule;
import com.seatup.seat.dto.SeatGradeResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ScheduleDetailResponse {

    private Long id;

    private LocalDate performanceDate;

    private LocalTime performanceTime;

    private int round;

    private List<SeatGradeResponse> seatGrades;

    public ScheduleDetailResponse(Long id, LocalDate performanceDate, LocalTime performanceTime, int round) {
        this.id = id;
        this.performanceDate = performanceDate;
        this.performanceTime = performanceTime;
        this.round = round;
    }

    public static ScheduleDetailResponse from(PerformanceSchedule schedule) {
        return new ScheduleDetailResponse(
                schedule.getId(),
                schedule.getPerformanceDate(),
                schedule.getPerformanceTime(),
                schedule.getRound());
    }
}
