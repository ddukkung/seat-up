package com.seatup.admin.seat.service;

import com.seatup.admin.seat.dto.RegisterSeatGradeRequest;
import com.seatup.admin.seat.dto.SeatGradeDto;
import com.seatup.admin.seat.dto.UpdateSeatGradeRequest;
import com.seatup.admin.seat.exception.DuplicateSeatGradeException;
import com.seatup.common.exception.BusinessException;
import com.seatup.admin.schedule.dto.RegisterScheduleRequest;
import com.seatup.admin.schedule.dto.UpdateScheduleRequest;
import com.seatup.performance.schedule.entity.PerformanceSchedule;
import com.seatup.seat.entity.SeatGrade;
import com.seatup.seat.repository.SeatGradeRepository;
import com.seatup.seat.enums.Grade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminSeatGradeService {

    private final SeatGradeRepository seatGradeRepository;

    public void registerSeatGrades(RegisterScheduleRequest scheduleRequest, PerformanceSchedule schedule) {
        List<RegisterSeatGradeRequest> seatGrades = scheduleRequest.getSeatGrades();

        if (seatGrades == null || seatGrades.isEmpty()) {
            throw new BusinessException("좌석 등급을 하나 이상 등록해주세요.");
        }

        validateSeatGrades(seatGrades);

        for (RegisterSeatGradeRequest seat : seatGrades) {
            saveSeatGrade(schedule, seat.getGrade(), seat.getPrice(), seat.getTotalQuantity());
        }
    }

    public void updateSeatGrades(UpdateScheduleRequest scheduleRequest, PerformanceSchedule schedule) {
        List<UpdateSeatGradeRequest> seatGradeReqs = scheduleRequest.getSeatGrades();

        if (seatGradeReqs == null || seatGradeReqs.isEmpty()) {
            throw new BusinessException("좌석 등급을 하나 이상 등록해주세요.");
        }

        validateSeatGrades(seatGradeReqs);

        // 요청에 포함되지 않은 기존 등급 삭제
        List<SeatGrade> existingGrades = seatGradeRepository.findByPerformanceScheduleId(schedule.getId());
        Set<Long> requestIds = seatGradeReqs.stream()
                .map(UpdateSeatGradeRequest::getId)
                .filter(id -> id != null)
                .collect(Collectors.toSet());

        for (SeatGrade existing : existingGrades) {
            if (!requestIds.contains(existing.getId())) {
                // 이미 예약이 발생한 등급은 삭제 불가
                if (existing.getTotalQuantity() != existing.getRemainQuantity()) {
                    throw new BusinessException(existing.getGrade() + " 등급은 이미 예약이 존재하여 삭제할 수 없습니다.");
                }
                seatGradeRepository.delete(existing);
            }
        }

        // 업데이트 및 추가
        for (UpdateSeatGradeRequest seatReq : seatGradeReqs) {
            if (seatReq.getId() != null) {
                SeatGrade seatGrade = seatGradeRepository.findById(seatReq.getId())
                        .orElseThrow(() -> new BusinessException("좌석 정보가 존재하지 않습니다."));

                int soldCount = seatGrade.getTotalQuantity() - seatGrade.getRemainQuantity();

                // 수정하려는 전체 좌석수가 이미 팔린 수보다 적으면 에러
                if (seatReq.getTotalQuantity() < soldCount) {
                    throw new BusinessException(seatReq.getGrade() + " 등급의 총 좌석 수는 판매된 좌석(" + soldCount + ")보다 적을 수 없습니다.");
                }

                int newRemainQuantity = seatReq.getTotalQuantity() - soldCount;

                seatGrade.update(
                        seatReq.getGrade(),
                        seatReq.getPrice(),
                        seatReq.getTotalQuantity(),
                        newRemainQuantity
                );
            } else {
                saveSeatGrade(schedule, seatReq.getGrade(), seatReq.getPrice(), seatReq.getTotalQuantity());
            }
        }
    }

    private void saveSeatGrade(PerformanceSchedule schedule, Grade grade, int price, int totalQuantity) {
        SeatGrade seatGrade = SeatGrade.builder()
                .performanceSchedule(schedule)
                .grade(grade)
                .price(price)
                .totalQuantity(totalQuantity)
                .build();
        seatGradeRepository.save(seatGrade);
    }

    /**
     * 등급 중복 검증
     * @param seatGrades
     */
    private void validateSeatGrades(List<? extends SeatGradeDto> seatGrades) {
        Set<Grade> grades = new HashSet<>();

        for (SeatGradeDto seat : seatGrades) {
            if (!grades.add(seat.getGrade())) {
                throw new DuplicateSeatGradeException();
            }
        }
    }
}
