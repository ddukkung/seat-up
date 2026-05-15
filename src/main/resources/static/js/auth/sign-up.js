"use strict";

import * as Validation from '/js/validation/auth-validation.js';

const SignUp = {
    init() {
        document.getElementById('signUpBtn').addEventListener('click', function (event) {
            let signUpDto = SignUp.getSignUpInputValues();

            // 입력값 검증을 통과했을 경우에만 회원가입 진행
            if (SignUp.validateSignUpForm(signUpDto)) {
                SignUp.submitSignUpData(signUpDto);
            }
        });
    },

    /**
     * 회원가입 입력값을 반환한다.
     */
    getSignUpInputValues() {
        return {
            loginId : document.getElementById('loginId').value.trim(),
            password : document.getElementById('password').value.trim(),
            confirmPassword : document.getElementById('confirmPassword').value.trim(),
            name : document.getElementById('name').value.trim(),
            email : document.getElementById('email').value.trim(),
            address : document.getElementById('address').value.trim(),
            phoneNumber : document.getElementById('phoneNumber').value.trim()
        }
    },

    validateSignUpForm(signUpDto) {
        // 입력값 검증
        if (!signUpDto.loginId || !signUpDto.password || !signUpDto.confirmPassword || !signUpDto.name || !signUpDto.email ) {
            alert('필수 입력값을 입력해주십시오.');
            return false;
        }

        if (!Validation.validate('loginId', signUpDto.loginId)) {
            alert('아이디가 유효하지 않습니다.');
            return false;
        }

        if (!Validation.validate('password', signUpDto.password)) {
            alert('비밀번호가 유효하지 않습니다.');
            return false;
        }

        if (!Validation.validate('name', signUpDto.name)) {
            alert('이름이 유효하지 않습니다.');
            return false;
        }

        if (signUpDto.email && !Validation.validate('email', signUpDto.email)) {
            alert('이메일이 유효하지 않습니다.');
            return false;
        }

        if (signUpDto.phoneNumber && !Validation.validate('phoneNumber', signUpDto.phoneNumber)) {
            alert('핸드폰 번호가 유효하지 않습니다.');
            return false;
        }

        return true;
    },

    submitSignUpData(signUpDto) {
        delete signUpDto.confirmPassword;

        fetch('/api/auth/sign-up', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(signUpDto)
        })
        .then(async response => {
            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.message || '회원가입에 실패했습니다.');
            }

            alert('회원가입 성공');
            window.location = '/auth/login';
        })
        .catch(error => {
            alert(`회원가입 실패: ${error.message}`);
            console.error('회원가입 실패: ', error.message);
        });
    }
}

document.addEventListener('DOMContentLoaded', () => SignUp.init());