"use strict";

import {authFetch} from '/js/common/authFetch.js';

const Detail = {

    init() {
        document.getElementById('btn-pay')?.addEventListener('click', () => this.requestPayment());
        document.getElementById('btn-cancel')?.addEventListener('click', () => this.cancelPayment());
    },

    async requestPayment() {
        const tossPayments = TossPayments('test_ck_BX7zk2yd8yOmOqZ7wm9pVx9POLqK');
        const payment = tossPayments.payment({customerKey: 'ANONYMOUS'});

        const reservationId = document.getElementById('reservation-id').dataset.reservationId;
        const performanceTitle = document.getElementById('performance-title').textContent;
        const amount = Number(document.getElementById('total-price').dataset.amount);
        const orderId = `ORDER_${reservationId}_${Date.now()}`;

        // 서버에 orderId, amount 저장
        try {
            const res = await authFetch('/api/payments/ready', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({reservationId: Number(reservationId), amount, orderId})
            });
            if (!res.ok) throw new Error((await res.json()).message || '결제 준비에 실패했습니다.');
        } catch(err) {
            alert(err.message);
            return;
        }

        // 토스 결제 API 호출
        await payment.requestPayment({
            method: 'CARD',
            amount: {currency: 'KRW', value: amount},
            orderId: orderId,
            orderName: performanceTitle,
            successUrl: `${window.location.origin}/users/mypage/reservations/payment/success`,
            failUrl: `${window.location.origin}/users/mypage/reservations/payment/fail`,
        });
    },

    async cancelPayment() {
        if (!confirm('예매를 취소하시겠습니까? 취소 후에는 되돌릴 수 없습니다.')) return;

        const reservationId = Number(document.getElementById('reservation-id').dataset.reservationId);

        try {
            const res = await authFetch('/api/payments/cancel', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({reservationId})
            });
            if (!res.ok) throw new Error((await res.json()).message || '결제 취소에 실패했습니다.');
            alert('예매가 취소되었습니다.');
            location.href = '/users/mypage/reservations';
        } catch(err) {
            alert(err.message);
            return;
        }
    },

}

document.addEventListener('DOMContentLoaded', () => {Detail.init()});