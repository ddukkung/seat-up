package com.seatup.performance.dto;

import com.seatup.performance.entity.Performance;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PerformanceListResponse {

    private Long id;

    private String title;

    private LocalDateTime startDateTime;

    private String posterUrl;

    public static PerformanceListResponse from(Performance performance) {
        return new PerformanceListResponse(
                performance.getId(),
                performance.getTitle(),
                performance.getStartDateTime(),
                performance.getPosterUrl()
        );
    }

}
