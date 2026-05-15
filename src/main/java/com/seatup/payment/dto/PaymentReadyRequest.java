package com.seatup.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentReadyRequest {

    private Long reservationId;

    private int amount;

    private String orderId;

}
