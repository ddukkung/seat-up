package com.seatup.performance.service;

import com.seatup.common.exception.BusinessException;
import com.seatup.common.performance.PerformanceQueryService;
import com.seatup.performance.entity.Performance;
import com.seatup.performance.dto.PerformanceDetailResponse;
import com.seatup.performance.dto.PerformanceListResponse;
import com.seatup.performance.repository.PerformanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerformanceService {

    private final PerformanceRepository performanceRepository;

    public PerformanceService(PerformanceRepository performanceRepository) {
        this.performanceRepository = performanceRepository;
    }

    /**
     * 공연 분류에 맞는 공연 리스트를 반환한다.
     * @param categoryId
     * @return
     */
    public List<PerformanceListResponse> findPerformancesByCategory(Long categoryId) {
        return performanceRepository.findByCategoryId(categoryId)
                .stream()
                .map(PerformanceListResponse::from)
                .toList();
    }

    /**
     * 삭제 상태를 포함한 모든 공연 리스트를 반환한다.
     * @return
     */
    // TODO : 엔티티 -> DTO...^^
    public List<Performance> getPerformanceList() {
        return performanceRepository.findAll();
    }

    public PerformanceDetailResponse findById(Long id) {
        Performance performance = performanceRepository.findByIdWithSchedules(id)
                .orElseThrow(() -> new BusinessException("해당 공연이 존재하지 않습니다."));
        return PerformanceDetailResponse.from(performance);
    }
}
