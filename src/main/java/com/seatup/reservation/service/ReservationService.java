package com.seatup.reservation.service;

import com.seatup.common.exception.BusinessException;
import com.seatup.payment.entity.Payment;
import com.seatup.payment.repository.PaymentRepository;
import com.seatup.performance.entity.Performance;
import com.seatup.performance.repository.PerformanceRepository;
import com.seatup.performance.schedule.entity.PerformanceSchedule;
import com.seatup.performance.schedule.repository.PerformanceScheduleRepository;
import com.seatup.reservation.entity.Reservation;
import com.seatup.reservation.dto.ReservationListResponse;
import com.seatup.reservation.dto.ReservationRequest;
import com.seatup.reservation.repository.ReservationRepository;
import com.seatup.seat.entity.SeatGrade;
import com.seatup.seat.repository.SeatGradeRepository;
import com.seatup.user.entity.User;
import com.seatup.user.mypage.dto.ReservationDetailResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final PerformanceRepository performanceRepository;
    private final PerformanceScheduleRepository scheduleRepository;
    private final SeatGradeRepository seatGradeRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public void reserve(User user, @Valid ReservationRequest request) {
        Performance performance = performanceRepository.findById(request.getPerformanceId())
                .orElseThrow(() -> new BusinessException("공연 정보가 존재하지 않습니다."));

        PerformanceSchedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new BusinessException("회차 정보가 존재하지 않습니다."));

        SeatGrade seat = seatGradeRepository.findById(request.getSeatGradeId())
                .orElseThrow(() -> new BusinessException("좌석 정보가 존재하지 않습니다."));

        if (seat.getRemainQuantity() < request.getQuantity()) {
            throw new BusinessException("잔여 좌석이 부족합니다.");
        }

        int unitPrice = seat.getPrice();
        int totalPrice = unitPrice * request.getQuantity();

        Reservation reservation = Reservation.builder()
                .user(user)
                .performance(performance)
                .schedule(schedule)
                .seatGrade(seat)
                .quantity(request.getQuantity())
                .unitPrice(unitPrice)
                .totalPrice(totalPrice)
                .deliveryType(request.getDeliveryType())
                .build();

        seat.decreaseRemainQuantity(request.getQuantity());
        reservationRepository.save(reservation);
    }

    public List<ReservationListResponse> findByUserId(User user) {
        return reservationRepository.findAllForList(user.getId())
                .stream()
                .map(ReservationListResponse::from)
                .toList();
    }

    public ReservationDetailResponse findById(User user, Long id) {
        Reservation reservation = reservationRepository.findForDetail(id)
                .orElseThrow(() -> new BusinessException("예약 정보가 존재하지 않습니다."));
        Payment payment = paymentRepository.findFirstByReservationOrderByCreatedAtDesc(reservation).orElse(null);
        return ReservationDetailResponse.from(user, reservation, payment);
    }
}
