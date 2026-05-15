package com.seatup.reservation.enums;

public enum ReservationStatus {
    PENDING("결제 대기"),
    CONFIRMED("예매 완료"),
    CANCELED("취소 완료"),
    EXPIRED("입금 기간 만료");

    private final String label;

    ReservationStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
