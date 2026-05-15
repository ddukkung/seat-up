package com.seatup.reservation.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seatup.payment.entity.QPayment;
import com.seatup.payment.enums.PaymentStatus;
import com.seatup.reservation.entity.QReservation;
import com.seatup.reservation.entity.Reservation;
import com.seatup.reservation.enums.ReservationStatus;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Reservation> findExpiredReservations(LocalDateTime now) {
        QReservation reservation = QReservation.reservation;
        QPayment payment = QPayment.payment;

        return queryFactory
                .selectFrom(reservation)
                .leftJoin(payment).on(payment.reservation.id.eq(reservation.id))
                .where(
                        reservation.status.eq(ReservationStatus.PENDING),
                        reservation.expiredAt.before(now),
                        payment.id.isNull().or(payment.status.eq(PaymentStatus.FAILED))
                )
                .fetch();
    }

}
