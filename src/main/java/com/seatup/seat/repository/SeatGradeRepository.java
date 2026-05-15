package com.seatup.seat.repository;

import com.seatup.seat.entity.SeatGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatGradeRepository extends JpaRepository<SeatGrade, Long> {
    List<SeatGrade> findByPerformanceScheduleId(Long id);

    @Modifying
    @Query("delete from SeatGrade s where s.performanceSchedule.id = :scheduleId")
    void deleteByScheduleId(@Param("scheduleId") Long scheduleId);

}

