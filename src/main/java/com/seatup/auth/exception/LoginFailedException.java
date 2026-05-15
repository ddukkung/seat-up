package com.seatup.auth.exception;

import com.seatup.common.exception.BusinessException;

public class LoginFailedException extends BusinessException {
    public LoginFailedException() {
        super("아이디 또는 비밀번호가 일치하지 않습니다.");
    }
}
