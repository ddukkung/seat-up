package com.seatup.auth.exception;

import com.seatup.common.exception.BusinessException;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException() {
        super("회원이 존재하지 않습니다.");
    }
}
