package com.seatup.admin.schedule.service;

import com.seatup.admin.schedule.dto.*;
import com.seatup.admin.seat.service.AdminSeatGradeService;
import com.seatup.common.exception.BusinessException;
import com.seatup.performance.entity.Performance;
import com.seatup.performance.enums.PerformanceStatus;
import com.seatup.performance.repository.PerformanceRepository;
import com.seatup.performance.schedule.entity.PerformanceSchedule;
import com.seatup.performance.schedule.repository.PerformanceScheduleRepository;
import com.seatup.seat.dto.SeatGradeResponse;
import com.seatup.seat.entity.SeatGrade;
import com.seatup.seat.repository.SeatGradeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminPerformScheduleService {

    private final PerformanceScheduleRepository scheduleRepository;
    private final PerformanceRepository performanceRepository;
    private final AdminSeatGradeService seatGradeService;
    private final SeatGradeRepository seatGradeRepository;

    public AdminPerformScheduleService(PerformanceScheduleRepository scheduleRepository, PerformanceRepository performanceRepository,
                                       AdminSeatGradeService seatGradeService, SeatGradeRepository seatGradeRepository) {
        this.scheduleRepository = scheduleRepository;
        this.performanceRepository = performanceRepository;
        this.seatGradeService = seatGradeService;
        this.seatGradeRepository = seatGradeRepository;
    }

    public AdminScheduleListResponse findSchedules(Long id) {
        List<PerformanceSchedule> scheduleEntityList = scheduleRepository.findByPerformanceId(id);

        List<ScheduleSummaryResponse> schedules = scheduleEntityList.stream()
                .map(ScheduleSummaryResponse::from)
                .toList();

        return new AdminScheduleListResponse(schedules);
    }

    @Transactional
    public String save(RegisterScheduleRequest request) {
        Performance performance = performanceRepository.findById(request.getPerformanceId())
                .orElseThrow(() -> new BusinessException("해당 공연이 존재하지 않습니다."));

        int round = scheduleRepository.findNextRound(performance.getId());

        PerformanceSchedule schedule = PerformanceSchedule.builder()
                .performance(performance)
                .performanceDate(request.getPerformanceDate())
                .performanceTime(request.getPerformanceTime())
                .round(round)
                .build();

        PerformanceSchedule saved = scheduleRepository.save(schedule);
        seatGradeService.registerSeatGrades(request, saved);

        if (performance.getStatus().equals(PerformanceStatus.DRAFT)) {
            performance.schedule();
        }

        return performance.getStatus().name();
    }

    public AdminScheduleResponse findSchedule(Long id) {
        PerformanceSchedule performanceSchedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("회차 정보가 존재하지 않습니다."));

        List<SeatGrade> seatGradeEntityList = seatGradeRepository.findByPerformanceScheduleId(id);
        List<SeatGradeResponse> seatGrades = SeatGradeResponse.from(seatGradeEntityList);

        return AdminScheduleResponse.from(performanceSchedule, seatGrades);
    }

    @Transactional
    public void updateSchedule(Long id, UpdateScheduleRequest request) {
        Performance performance = performanceRepository.findById(request.getPerformanceId())
                .orElseThrow(() -> new BusinessException("해당 공연이 존재하지 않습니다."));

        PerformanceSchedule scheduleEntity = scheduleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("공연 회차 정보가 존재하지 않습니다."));

        scheduleEntity.update(
                performance,
                request.getPerformanceDate(),
                request.getPerformanceTime()
        );

        if (request.getSeatGrades().isEmpty()) {
            throw new BusinessException("좌석 등급 정보를 입력해주세요.");
        }
        seatGradeService.updateSeatGrades(request, scheduleEntity);
    }

    @Transactional
    public void deleteSchedule(Long id) {
        PerformanceSchedule performanceSchedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("해당 회차가 존재하지 않습니다."));

        seatGradeRepository.deleteByScheduleId(id);
        scheduleRepository.delete(performanceSchedule);
    }
}
