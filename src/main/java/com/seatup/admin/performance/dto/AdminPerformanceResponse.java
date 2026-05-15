package com.seatup.admin.performance.dto;

import com.seatup.performance.entity.Performance;
import com.seatup.performance.enums.PerformanceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdminPerformanceResponse {

    private Long id;

    private Long categoryId;

    private String title;

    private String description;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private LocalDateTime openDateTime;

    private LocalDateTime closeDateTime;

    private String venue;

    private PerformanceStatus status;

    private String posterUrl;

    public static AdminPerformanceResponse from(Performance performance) {
        return new AdminPerformanceResponse(
                performance.getId(),
                performance.getCategory().getId(),
                performance.getTitle(),
                performance.getDescription(),
                performance.getStartDateTime(),
                performance.getEndDateTime(),
                performance.getOpenDateTime(),
                performance.getCloseDateTime(),
                performance.getVenue(),
                performance.getStatus(),
                performance.getPosterUrl()
        );
    }

}
