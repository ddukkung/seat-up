package com.seatup.performance.enums;

public enum PerformanceStatus {
    DRAFT("임시저장"),
    SCHEDULED("예정"),
    OPEN("예매중"),
    CLOSED("종료");

    private final String label;

    PerformanceStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
