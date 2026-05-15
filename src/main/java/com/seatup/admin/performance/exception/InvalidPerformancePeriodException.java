package com.seatup.admin.performance.exception;

import com.seatup.common.exception.BusinessException;

public class InvalidPerformancePeriodException extends BusinessException {
    public InvalidPerformancePeriodException(String message) {
        super(message);
    }
}
