"use strict";

import {isEmpty} from '/js/validation/auth-validation.js';

const Login = {
    init() {
        document.getElementById('loginBtn').addEventListener('click', Login.handleLogin);
        document.addEventListener('keydown', function (e) {
            if (e.key === 'Enter') Login.handleLogin();
        });
    },

    handleLogin() {
        let loginDto = Login.getLoginInputValues();
        if (Login.validateLoginForm(loginDto)) {
            Login.login(loginDto);
        }
    },

    getLoginInputValues() {
        return {
            loginId: document.getElementById('loginId').value.trim(),
            password: document.getElementById('password').value.trim()
        }
    },

    validateLoginForm(loginDto) {
        if (isEmpty(loginDto.loginId)) {
            document.getElementById('errorMessage').textContent = '아이디를 입력해주세요.';
            document.getElementById('loginId').focus();
            return false;
        } else if (isEmpty(loginDto.password)) {
            document.getElementById('errorMessage').textContent = '비밀번호를 입력해주세요.';
            document.getElementById('password').focus();
            return false;
        }

        document.getElementById('errorMessage').textContent = '';
        return true;
    },

    login(loginDto) {
        fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(loginDto)
        })
        .then(async response => {
            if (!response.ok) {
                throw new Error();
            }

            const authHeader = response.headers.get('Authorization');
            if (authHeader && authHeader.startsWith('Bearer ')) {
                const accessToken = authHeader.substring(7);
                localStorage.setItem('accessToken', accessToken);
            }

            const data = await response.json();
            if (data.refreshToken) {
                localStorage.setItem('refreshToken', data.refreshToken);
            }

            window.location = '/';
        })
        .catch(error => {
            alert('아이디 또는 비밀번호가 일치하지 않습니다.');
        });
    }
}

document.addEventListener('DOMContentLoaded', () => Login.init());