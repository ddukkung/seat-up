package com.seatup.admin.seat.exception;

import com.seatup.common.exception.BusinessException;

public class DuplicateSeatGradeException extends BusinessException {
    public DuplicateSeatGradeException() {
        super("동일한 좌석 등급이 중복되었습니다.");
    }
}
