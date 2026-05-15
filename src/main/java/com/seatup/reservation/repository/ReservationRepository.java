package com.seatup.reservation.repository;

import com.seatup.reservation.entity.Reservation;
import com.seatup.reservation.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationRepositoryCustom {

    @Query("SELECT r FROM Reservation r LEFT JOIN FETCH r.performance WHERE r.user.id = :userId")
    List<Reservation> findAllForList(@Param("userId") Long userId);

    @Query("SELECT r FROM Reservation r "
            + "LEFT JOIN FETCH r.performance LEFT JOIN FETCH r.schedule LEFT JOIN FETCH r.seatGrade "
            + "WHERE r.id = :id")
    Optional<Reservation> findForDetail(@Param("id") Long id);

    @Query("SELECT r FROM Reservation r "
            + "LEFT JOIN FETCH r.user LEFT JOIN FETCH r.performance LEFT JOIN FETCH r.seatGrade "
            + "WHERE r.id = :id AND r.user.id = :userId")
    Optional<Reservation> findForPayment(@Param("id") Long id, @Param("userId") Long userId);

    boolean existsByPerformanceIdAndStatusNot(Long performanceId, ReservationStatus status);
}
