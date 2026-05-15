package com.seatup.reservation.enums;

public enum DeliveryType {
    MOBILE("모바일티켓"),
    ONSITE("현장수령"),
    DELIVERY("배송");

    private final String label;

    DeliveryType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
