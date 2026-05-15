package com.seatup.auth.exception;

import com.seatup.common.exception.BusinessException;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException() {
        super("접근 권한이 없습니다.");
    }
}
