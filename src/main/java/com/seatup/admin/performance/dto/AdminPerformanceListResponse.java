package com.seatup.admin.performance.dto;

import com.seatup.performance.entity.Performance;
import com.seatup.performance.enums.PerformanceDeleteStatus;
import com.seatup.performance.enums.PerformanceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdminPerformanceListResponse {

    private Long id;

    private String title;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private LocalDateTime openDateTime;

    private PerformanceStatus status;

    private PerformanceDeleteStatus isDeleted;

    public static AdminPerformanceListResponse from(Performance performance) {
        return new AdminPerformanceListResponse(
                performance.getId(),
                performance.getTitle(),
                performance.getStartDateTime(),
                performance.getEndDateTime(),
                performance.getOpenDateTime(),
                performance.getStatus(),
                performance.getIsDeleted()
        );
    }

}
