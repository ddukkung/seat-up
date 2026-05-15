package com.seatup.reservation.entity;

import com.seatup.common.exception.BusinessException;
import com.seatup.performance.entity.Performance;
import com.seatup.performance.schedule.entity.PerformanceSchedule;
import com.seatup.reservation.enums.DeliveryType;
import com.seatup.reservation.enums.ReservationStatus;
import com.seatup.seat.entity.SeatGrade;
import com.seatup.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "RESERVATION")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERFORMANCE_ID", nullable = false)
    private Performance performance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHEDULE_ID", nullable = false)
    private PerformanceSchedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEAT_GRADE_ID", nullable = false)
    private SeatGrade seatGrade;

    @Column(name = "QUANTITY", nullable = false)
    private int quantity;

    @Column(name = "UNIT_PRICE", nullable = false)
    private int unitPrice;

    @Column(name = "TOTAL_PRICE", nullable = false)
    private int totalPrice;

    @Column(name = "DELIVERY_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Column(name = "EXPIRED_AT", nullable = false)
    private LocalDateTime expiredAt;

    @Column(name = "RESERVED_AT", nullable = false)
    private LocalDateTime reservedAt;

    @Builder
    public Reservation(User user, Performance performance, PerformanceSchedule schedule, SeatGrade seatGrade,
                       int quantity, int unitPrice, int totalPrice, DeliveryType deliveryType) {
        this.user = user;
        this.performance = performance;
        this.schedule = schedule;
        this.seatGrade = seatGrade;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.deliveryType = deliveryType;
        this.status = ReservationStatus.PENDING;
        this.reservedAt = LocalDateTime.now();

        LocalDateTime sameDayDeadline = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);
        LocalDateTime minimumDeadline = LocalDateTime.now().plusHours(1);
        this.expiredAt = sameDayDeadline.isAfter(minimumDeadline) ? sameDayDeadline : minimumDeadline;
    }

    public void confirm() {
        this.status = ReservationStatus.CONFIRMED;
    }

    public void expire() {
        if (this.status != ReservationStatus.PENDING) {
            throw new BusinessException("취소 가능한 상태가 아닙니다. 현재 상태: " + this.status);
        }
        this.status = ReservationStatus.EXPIRED;
    }

    public void cancel() {
        if (this.status != ReservationStatus.CONFIRMED) {
            throw new BusinessException("취소 가능한 상태가 아닙니다.");
        }
        this.status = ReservationStatus.CANCELED;
    }

}
