package com.seatup.performance.schedule.service;

import com.seatup.performance.schedule.dto.PerformanceBookingResponse;
import com.seatup.performance.schedule.dto.ScheduleDetailResponse;
import com.seatup.performance.schedule.entity.PerformanceSchedule;
import com.seatup.performance.schedule.repository.PerformanceScheduleRepository;
import com.seatup.seat.dto.SeatGradeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PerformanceScheduleService {

    private final PerformanceScheduleRepository scheduleRepository;

    @Transactional(readOnly = true)
    public PerformanceBookingResponse getSchedules(Long performanceId) {
        List<PerformanceSchedule> schedules = scheduleRepository.findAllWithSeatGrades(performanceId);

        List<ScheduleDetailResponse> schedulesDtos = schedules.stream()
                .map(s -> new ScheduleDetailResponse(
                        s.getId(),
                        s.getPerformanceDate(),
                        s.getPerformanceTime(),
                        s.getRound(),
                        SeatGradeResponse.from(s.getSeatGrades())
                ))
                .toList();

        return new PerformanceBookingResponse(performanceId, schedulesDtos);
    }
}
