package com.seatup.common.performance;

import com.seatup.admin.performance.exception.PerformanceNotFoundException;
import com.seatup.performance.entity.Performance;
import com.seatup.performance.repository.PerformanceRepository;
import org.springframework.stereotype.Service;

@Service
public class PerformanceQueryService {

    private final PerformanceRepository performanceRepository;

    public PerformanceQueryService(PerformanceRepository performanceRepository) {
        this.performanceRepository = performanceRepository;
    }

    public Performance findById(Long id) {
        return performanceRepository.findByIdAndNotDeleted(id).orElseThrow(PerformanceNotFoundException::new);
    }
}
