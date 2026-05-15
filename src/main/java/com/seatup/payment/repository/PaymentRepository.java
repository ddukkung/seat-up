package com.seatup.payment.repository;

import com.seatup.payment.entity.Payment;
import com.seatup.payment.enums.PaymentStatus;
import com.seatup.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // 특정 예약에 대한 가장 최근의 성공한 결제 정보 조회
    Optional<Payment> findFirstByReservationOrderByCreatedAtDesc(Reservation reservation);

    @Query("select p from Payment p join fetch p.reservation where p.id = :id")
    Optional<Payment> findByIdWithReservation(@Param("id") Long id);

    Optional<Payment> findByOrderId(String orderId);

    Optional<Payment> findByReservationIdAndStatus(Long reservationId, PaymentStatus status);
}
