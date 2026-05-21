package com.seatup.admin.performance.service;

import com.seatup.admin.performance.dto.AdminPerformanceListResponse;
import com.seatup.admin.performance.dto.AdminPerformanceResponse;
import com.seatup.admin.performance.dto.UpdatePerformanceRequest;
import com.seatup.admin.performance.exception.PerformanceNotFoundException;
import com.seatup.category.entity.Category;
import com.seatup.category.repository.CategoryRepository;
import com.seatup.common.exception.BusinessException;
import com.seatup.performance.entity.Performance;
import com.seatup.performance.repository.PerformanceRepository;
import com.seatup.performance.service.PerformanceService;
import com.seatup.admin.performance.dto.RegisterPerformanceRequest;
import com.seatup.admin.performance.exception.InvalidPerformancePeriodException;
import com.seatup.reservation.enums.ReservationStatus;
import com.seatup.reservation.repository.ReservationRepository;
import com.seatup.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminPerformService {

    private final PerformanceService performanceService;
    private final PerformanceRepository performanceRepository;
    private final CategoryRepository categoryRepository;
    private final ReservationRepository reservationRepository;

    public List<AdminPerformanceListResponse> getPerformanceList() {
        List<Performance> performanceEntities = performanceService.getPerformanceList();
        return performanceEntities.stream()
                .map(AdminPerformanceListResponse::from)
                .toList();
    }

    /**
     * 공연을 신규 등록한다.
     *
     * @param user
     * @param request
     * @return
     */
    @Transactional
    public Long registerPerformance(User user, RegisterPerformanceRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BusinessException("존재하지 않는 카테고리입니다."));

        validatePerformanceData(request);

        Performance performance = Performance.create(
                category,
                request.getTitle(),
                request.getDescription(),
                request.getStartDateTime(),
                request.getEndDateTime(),
                request.getOpenDateTime(),
                request.getCloseDateTime(),
                request.getVenue(),
                user.getId(),
                request.getPosterUrl()
        );

        performanceRepository.save(performance);
        return performance.getId();
    }

    private void validatePerformanceData(RegisterPerformanceRequest request) {
        LocalDateTime start = request.getStartDateTime();
        LocalDateTime end = request.getEndDateTime();

        if (start.isBefore(LocalDateTime.now())) {
            throw new InvalidPerformancePeriodException("공연 시작 일시는 현재 시점 이후여야 합니다.");
        }

        if (start.isAfter(end)) {
            throw new InvalidPerformancePeriodException("공연 시작 일시는 종료 일시보다 이전이어야 합니다.");
        }
    }

    @Transactional
    public void updatePerformance(@Valid UpdatePerformanceRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BusinessException("존재하지 않는 카테고리입니다."));

        Performance performance = performanceRepository.findById(request.getId())
                .orElseThrow(PerformanceNotFoundException::new);

        performance.update(
                category,
                request.getTitle(),
                request.getDescription(),
                request.getStartDateTime(),
                request.getEndDateTime(),
                request.getOpenDateTime(),
                request.getCloseDateTime(),
                request.getVenue(),
                request.getPosterUrl() != null ? request.getPosterUrl() : performance.getPosterUrl()
        );
    }

    @Transactional
    public void deletePerformance(Long id) {
        Performance performance = performanceRepository.findById(id).orElseThrow(PerformanceNotFoundException::new);
        if (reservationRepository.existsByPerformanceIdAndStatusNot(id, ReservationStatus.CANCELED)) {
            throw new BusinessException("예매 내역이 존재하는 공연은 삭제할 수 없습니다.");
        }

        performance.delete();
    }

    public AdminPerformanceResponse findPerformance(Long id) {
        Performance performance = performanceRepository.findById(id).orElseThrow(PerformanceNotFoundException::new);
        return AdminPerformanceResponse.from(performance);
    }
}
