package com.seatup.reservation.dto;

import com.seatup.reservation.enums.DeliveryType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationRequest {

    @NotNull
    private Long performanceId;

    @NotNull
    private Long scheduleId;

    @NotNull
    private Long seatGradeId;

    @Min(1)
    private int quantity;

    @NotNull
    private DeliveryType deliveryType;

}
