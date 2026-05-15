package com.seatup.admin.schedule.dto;

import com.seatup.performance.schedule.entity.PerformanceSchedule;
import com.seatup.seat.dto.SeatGradeResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminScheduleResponse {

    private Long id;

    private Long performanceId;

    private LocalDate performanceDate;

    private LocalTime performanceTime;

    private int round;

    private List<SeatGradeResponse> seatGrades;

    public AdminScheduleResponse(Long id, Long performanceId, LocalDate performanceDate, LocalTime performanceTime,
                                 int round, List<SeatGradeResponse> seatGrades) {
        this.id = id;
        this.performanceId = performanceId;
        this.performanceDate = performanceDate;
        this.performanceTime = performanceTime;
        this.round = round;
        this.seatGrades = seatGrades;
    }

    public static AdminScheduleResponse from(PerformanceSchedule entity, List<SeatGradeResponse> seatGrades) {
        return new AdminScheduleResponse(
                entity.getId(),
                entity.getPerformance().getId(),
                entity.getPerformanceDate(),
                entity.getPerformanceTime(),
                entity.getRound(),
                seatGrades
        );
    }
}
