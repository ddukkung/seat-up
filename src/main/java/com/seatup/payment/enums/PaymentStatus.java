package com.seatup.payment.enums;

public enum PaymentStatus {
    READY("결제 대기"),
    SUCCESS("결제 완료"),
    FAILED("결제 실패"),
    CANCELED("결제 취소");

    private final String label;

    PaymentStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
