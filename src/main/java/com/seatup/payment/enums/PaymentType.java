package com.seatup.payment.enums;

public enum PaymentType {
    CARD("신용/체크카드"),
    BANK_TRANSFER("무통장 입금"),
    VIRTUAL_ACCOUNT("가상계좌"),
    FREE("전액 포인트/초대권");

    private final String label;

    PaymentType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
