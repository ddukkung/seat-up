package com.seatup.performance.enums;

public enum PerformanceDeleteStatus {
    Y("삭제"),
    N("정상");

    private final String label;

    PerformanceDeleteStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
