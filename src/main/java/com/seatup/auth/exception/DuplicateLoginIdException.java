package com.seatup.auth.exception;

import com.seatup.common.exception.BusinessException;

public class DuplicateLoginIdException extends BusinessException {
    public DuplicateLoginIdException() {
        super("이미 존재하는 아이디입니다");
    }
}
