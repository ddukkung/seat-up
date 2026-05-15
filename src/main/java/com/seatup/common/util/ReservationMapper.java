package com.seatup.common.util;

import com.seatup.reservation.enums.ReservationStatus;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ReservationMapper {
    public static final DateTimeFormatter DATE_WITH_DAY = DateTimeFormatter.ofPattern("yyyy.MM.dd (E)", Locale.KOREAN);
    public static final DateTimeFormatter DATE_TIME_WITH_DAY = DateTimeFormatter.ofPattern("yyyy.MM.dd (E) HH:mm", Locale.KOREAN);
    public static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy.MM.dd (E)", Locale.KOREAN);
    public static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm");

    public static String getStatusClass(ReservationStatus status) {
        return switch (status.name()) {
            case "CONFIRMED" -> "status-confirmed";
            case "PENDING" -> "status-pending";
            case "CANCELED" -> "status-canceled";
            default -> "status-expired";
        };
    }
}
