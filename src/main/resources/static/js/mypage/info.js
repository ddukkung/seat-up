"use strict";

import {authFetch} from '/js/common/authFetch.js';
import * as Validation from '/js/validation/auth-validation.js';

const Info = {
    originalValues: {},
    init() {
        Info.originalValues = this.getValues();
        document.getElementById('btn-reset').addEventListener('click', () => this.resetForm());
        document.getElementById('btn-update-profile').addEventListener('click', () => this.updateProfile());
        document.getElementById('btn-update-password').addEventListener('click', () => this.updatePassword());
        document.getElementById('btn-withdraw').addEventListener('click', () => this.withdraw());
    },

    resetForm() {
        document.getElementById('name').value = Info.originalValues.name;
        document.getElementById('phone-number').value = Info.originalValues.phoneNumber;
        document.getElementById('email').value = Info.originalValues.email;
        document.getElementById('address').value = Info.originalValues.address;
    },

    async updateProfile() {
        const values = Info.getValues();
        if (!Info.validate(values)) return;

        const result = await authFetch('/api/users/profile', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(values)
        });

        if (result.ok) {
            alert('수정되었습니다.');
        } else {
            alert('수정에 실패했습니다.');
        }
    },

    validate(values) {
        if (!values.name || !values.email ) {
            alert('필수 입력값을 입력해주십시오.');
            return false;
        }

        if (!Validation.validate('name', values.name)) {
            alert('이름이 유효하지 않습니다.');
            return false;
        }

        if (!Validation.validate('phoneNumber', values.phoneNumber)) {
            alert('핸드폰 번호가 유효하지 않습니다.');
            return false;
        }

        if (!Validation.validate('email', values.email)) {
            alert('이메일이 유효하지 않습니다.');
            return false;
        }

        return true;
    },

    getValues() {
        return {
            name: document.getElementById('name').value,
            phoneNumber: document.getElementById('phone-number').value,
            email: document.getElementById('email').value,
            address: document.getElementById('address').value
        }
    },

    async updatePassword() {
        const newPassword = document.getElementById('new-password').value;
        const newPasswordConfirm = document.getElementById('new-password-confirm').value;

        if (!Validation.isPasswordMatched(newPassword, newPasswordConfirm)) {
            alert('새 비밀번호가 일치하지 않습니다.');
            return;
        }

        const result = await authFetch('/api/users/password', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                currentPassword: document.getElementById('current-password').value,
                newPassword: newPassword
            })
        });

        if (result.ok) {
            alert('비밀번호가 변경되었습니다.');
            document.getElementById('current-password').value = '';
            document.getElementById('new-password').value = '';
            document.getElementById('new-password-confirm').value = '';
        } else {
            alert('비밀번호 변경에 실패했습니다.');
        }
    },

    async withdraw() {
        if (!confirm('정말 탈퇴하시겠습니까? 모든 정보가 삭제됩니다.')) return;
        const result = await authFetch('/api/users/me', {
            method: 'DELETE'
        });
        if (result.ok) {
            localStorage.removeItem('accessToken');
            alert('탈퇴되었습니다.');
            window.location.href = '/';
        } else {
            alert('탈퇴에 실패했습니다.');
        }
    }
}

document.addEventListener('DOMContentLoaded', () => {Info.init()});