package com.seatup.performance.repository;

import com.seatup.performance.entity.Performance;
import com.seatup.performance.enums.PerformanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    @Query("SELECT p FROM Performance p "
            + "WHERE p.category.id = :categoryId AND p.isDeleted = 'N' AND p.status IN ('SCHEDULED', 'OPEN')")
    List<Performance> findByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT p FROM Performance p WHERE p.id = :id AND p.isDeleted = 'N'")
    Optional<Performance> findByIdAndNotDeleted(@Param("id") Long id);

    @Query("SELECT p FROM Performance p " +
            "JOIN FETCH p.schedules " +
            "WHERE p.id = :id AND p.isDeleted = 'N' AND p.status != 'DRAFT'")
    Optional<Performance> findByIdWithSchedules(@Param("id") Long id);

    List<Performance> findByStatusAndOpenDateTimeBefore(PerformanceStatus performanceStatus, LocalDateTime now);

    List<Performance> findByStatusAndCloseDateTimeBefore(PerformanceStatus performanceStatus, LocalDateTime now);
}
