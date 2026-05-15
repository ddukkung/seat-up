"use strict";

const signUpPatterns = {
    loginId: /^[a-zA-Z0-9]{4,12}$/,
    password: /^(?=.*[!#$])[a-zA-Z0-9!#$]{8,14}$/,
    name: /^[가-힣a-zA-Z]{2,30}$/,
    email: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
    phoneNumber: /^01[016789]-?\d{3,4}-?\d{4}$/
}

/**
 * 입력값 검증
 */
export function validate(type, value) {
    return signUpPatterns[type].test(value) ?? false;
}

/**
 * 비밀번호 입력값 일치 여부
 */
export function isPasswordMatched(password, confirmPassword) {
    password = password.trim();
    confirmPassword = confirmPassword.trim();

    // 비밀번호 입력값이 비어 있을 경우
    if (!password || !confirmPassword) {
        return false;
    }

    return password === confirmPassword;
}

export function isEmpty(value) {
    return value.trim() === "";
}