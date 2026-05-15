package com.seatup.reservation.dto;

import com.seatup.common.util.ReservationMapper;
import com.seatup.performance.entity.Performance;
import com.seatup.reservation.entity.Reservation;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReservationListResponse {

    private Long id;

    private String performanceTitle;

    private String formattedReservedAt;

    private String formattedStartDateTime;

    private int quantity;

    private String formattedExpiredAt;

    private String status;

    private String statusClass;


    public static ReservationListResponse from(Reservation reservation) {
        Performance performance = reservation.getPerformance();
        String cssClass = ReservationMapper.getStatusClass(reservation.getStatus());

        return ReservationListResponse.builder()
                .id(reservation.getId())
                .performanceTitle(performance.getTitle())
                .formattedReservedAt(reservation.getReservedAt().format(ReservationMapper.DATE_WITH_DAY))
                .formattedStartDateTime(performance.getStartDateTime().format(ReservationMapper.DATE_WITH_DAY))
                .quantity(reservation.getQuantity())
                .formattedExpiredAt(reservation.getExpiredAt().format(ReservationMapper.DATE_TIME_WITH_DAY) + " 까지")
                .status(reservation.getStatus().getLabel())
                .statusClass(cssClass)
                .build();
    }
}
