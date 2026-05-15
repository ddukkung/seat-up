package com.seatup.performance.schedule.repository;

import com.seatup.performance.schedule.entity.PerformanceSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PerformanceScheduleRepository extends JpaRepository<PerformanceSchedule, Long> {
    List<PerformanceSchedule> findByPerformanceId(Long id);

    @Query("SELECT DISTINCT ps FROM PerformanceSchedule ps " +
            "JOIN FETCH ps.seatGrades " +
            "WHERE ps.performance.id = :performanceId")
    List<PerformanceSchedule> findAllWithSeatGrades(@Param("performanceId") Long performanceId);

    @Query("SELECT COALESCE(MAX(s.round), 0) + 1 FROM PerformanceSchedule s WHERE s.performance.id = :performanceId")
    int findNextRound(@Param("performanceId") Long performanceId);

}
