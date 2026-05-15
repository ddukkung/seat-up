package com.seatup.payment.entity;

import com.seatup.payment.enums.PaymentStatus;
import com.seatup.payment.enums.PaymentType;
import com.seatup.reservation.entity.Reservation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "PAYMENT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESERVATION_ID", nullable = false)
    private Reservation reservation;

    @Column(name = "ORDER_ID", nullable = false)
    private String orderId;

    @Column(name = "AMOUNT", nullable = false)
    private int amount;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "PAYMENT_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(name = "PAYMENT_KEY", nullable = true)
    private String paymentKey;

    @Column(name = "FAIL_REASON", nullable = true)
    private String failReason;

    @Column(name = "PAYMENT_AT", nullable = true)
    private LocalDateTime paymentAt;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Payment(Reservation reservation, int amount, PaymentType paymentType, String orderId) {
        this.reservation = reservation;
        this.amount = amount;
        this.status = PaymentStatus.READY;
        this.paymentType = paymentType;
        this.createdAt = LocalDateTime.now();
        this.orderId = orderId;
    }

    public void complete(String paymentKey) {
        this.status = PaymentStatus.SUCCESS;
        this.paymentAt = LocalDateTime.now();
        this.paymentKey = paymentKey;
    }

    public void fail(String failReason) {
        this.status = PaymentStatus.FAILED;
        this.failReason = failReason;
    }

    public void cancel() {
        this.status = PaymentStatus.CANCELED;
    }

}
