package com.seatup.reservation.repository;

import com.seatup.reservation.entity.Reservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepositoryCustom {
    List<Reservation> findExpiredReservations(LocalDateTime now);
}
