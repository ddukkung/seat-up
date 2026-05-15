package com.seatup.admin.performance.exception;

import com.seatup.common.exception.BusinessException;

public class PerformanceNotFoundException extends BusinessException {
    public PerformanceNotFoundException() {
        super("존재하지 않는 공연입니다.");
    }
}
