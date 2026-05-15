package com.seatup.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class PaymentCancelRequest {
    private Long reservationId;
}
