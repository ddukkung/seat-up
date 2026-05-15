package com.seatup.seat.enums;

public enum Grade {
    VIP(1),
    R(2),
    S(3);

    private final int order;

    Grade(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }
}
