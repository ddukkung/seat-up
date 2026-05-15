package com.seatup.performance.dto;

import com.seatup.performance.entity.Performance;
import com.seatup.performance.enums.PerformanceStatus;
import com.seatup.performance.schedule.dto.ScheduleDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class PerformanceDetailResponse {

    private Long id;

    private Long categoryId;

    private String title;

    private String description;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private LocalDateTime openDateTime;

    private String venue;

    private PerformanceStatus status;

    private String posterUrl;

    private List<ScheduleDetailResponse> schedules;

    public static PerformanceDetailResponse from(Performance performance) {
        return new PerformanceDetailResponse(
                performance.getId(),
                performance.getCategory().getId(),
                performance.getTitle(),
                performance.getDescription(),
                performance.getStartDateTime(),
                performance.getEndDateTime(),
                performance.getOpenDateTime(),
                performance.getVenue(),
                performance.getStatus(),
                performance.getPosterUrl(),
                performance.getSchedules().stream()
                        .map(ScheduleDetailResponse::from)
                        .toList()
        );
    }

}
