"use strict";

import {authFetch} from '/js/common/authFetch.js';

const Detail = {
    selectedScheduleId: null,
    selectedGradeId: null,
    selectedPrice: 0,
    selectedRemain: 0,
    quantity: 1,
    schedulesData: [],
    token: null,

    init() {
        const accessToken = localStorage.getItem('accessToken');
        const reserveBtn = document.getElementById('btn-reserve');

        if (!accessToken) {
            reserveBtn.disabled = true;
            reserveBtn.textContent = '로그인 후 예매 가능';
        } else {
            document.getElementById('btn-reserve').addEventListener('click', async () => {
                const performanceId = document.getElementById('performance-id').value;

                const result = await authFetch(`/api/queue/${performanceId}/enter`, {
                    method: 'POST'
                });

                if (!result.ok) {
                    alert('대기열 진입에 실패했습니다.');
                    return;
                }

                const data = await result.json();
                if (data.immediate) {
                    // 바로 입장
                    Detail.token = data.token;
                    Detail.openModal();
                } else {
                    // 대기열 페이지로 이동
                    window.location.href = `/queue/waiting?performanceId=${performanceId}&token=${data.token}&rank=${data.rank}`;
                }
            });

            document.getElementById('modal-close-btn').addEventListener('click', () => Detail.closeModal());
            document.getElementById('reserve-btn').addEventListener('click', () => Detail.reserve());
            document.getElementById('modal-schedule-select').addEventListener('change', (e) => Detail.onScheduleChange(e));
            Detail.bindGradeSelect();
            Detail.bindQuantityControl();
        }

        // 대기 완료 후 입장 시 예매 모달 자동 오픈
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.get('openModal') === 'true') {
            Detail.token = urlParams.get('token');
            Detail.openModal();
        }
    },

    openModal() {
        Detail.resetModal();
        Detail.fetchSchedules();
        document.getElementById('reservation-modal').classList.remove('hidden');
    },

    closeModal() {
        document.getElementById('reservation-modal').classList.add('hidden');

        // 모달 닫기 시 대기열에서 제거
        if (Detail.token) {
            const performanceId = document.getElementById('performance-id').value;
            authFetch(`/api/queue/${performanceId}/leave-active?token=${Detail.token}`, {
                method: 'DELETE'
            });
            Detail.token = null;
        }
    },

    resetModal() {
        Detail.selectedScheduleId = null;
        Detail.selectedGradeId = null;
        Detail.selectedPrice = 0;
        Detail.selectedRemain = 0;
        Detail.quantity = 1;
        Detail.schedulesData = [];

        document.getElementById('modal-schedule-select').innerHTML = '<option value="">회차를 선택해주세요</option>';
        document.getElementById('modal-grade-list').innerHTML = '';
        document.getElementById('grade-selection-section').classList.add('hidden');
        document.getElementById('quantity-value').textContent = '1';
        document.getElementById('total-price').textContent = '0원';

        const reserveBtn = document.getElementById('reserve-btn');
        reserveBtn.disabled = true;
        reserveBtn.textContent = '회차를 선택해주세요';
    },

    async fetchSchedules() {
        const performanceId = document.getElementById('performance-id').value;
        const result = await authFetch(`/api/schedules/${performanceId}`);
        if (!result.ok) throw new Error('조회 실패');

        const data = await result.json();
        Detail.schedulesData = data.schedules;
        Detail.renderSchedules(data.schedules);
    },

    renderSchedules(schedules) {
        const select = document.getElementById('modal-schedule-select');

        schedules.forEach(s => {
            const option = document.createElement('option');
            option.value = s.id;
            option.textContent = `${s.round}회차 | ${s.performanceDate} ${s.performanceTime}`;
            select.appendChild(option);
        });
    },

    onScheduleChange(e) {
        const scheduleId = Number(e.target.value);
        Detail.selectedScheduleId = scheduleId || null;
        Detail.selectedGradeId = null;
        Detail.selectedPrice = 0;
        Detail.selectedRemain = 0;
        Detail.quantity = 1;

        const gradeSection = document.getElementById('grade-selection-section');

        if (!scheduleId) {
            gradeSection.classList.add('hidden');
            document.getElementById('modal-grade-list').innerHTML = '';
            Detail.updateUI();
            return;
        }

        const schedule = Detail.schedulesData.find(s => s.id === scheduleId);
        if (schedule) {
            Detail.renderGrades(schedule.seatGrades);
            gradeSection.classList.remove('hidden');
        }

        Detail.updateUI();
    },

    renderGrades(seatGrades) {
        const list = document.getElementById('modal-grade-list');
        list.innerHTML = '';

        seatGrades.forEach(grade => {
            const li = document.createElement('li');
            li.className = 'modal-grade-item' + (grade.remainQuantity === 0 ? ' sold-out' : '');
            li.dataset.gradeId = grade.id;
            li.dataset.price = grade.price;
            li.dataset.remain = grade.remainQuantity;

            li.innerHTML = `
                <span class="grade-name">${grade.grade}</span>
                <span class="grade-price">${grade.price.toLocaleString()}원</span>
                <span class="grade-remain">${grade.remainQuantity === 0 ? '매진' : '잔여 ' + grade.remainQuantity + '석'}</span>
            `;

            list.appendChild(li);
        });
    },

    bindGradeSelect() {

        document.getElementById('modal-grade-list').addEventListener('click', (e) => {
            const item = e.target.closest('.modal-grade-item');
            if (!item || item.classList.contains('sold-out')) return;

            document.querySelectorAll('.modal-grade-item').forEach(i => i.classList.remove('selected'));
            item.classList.add('selected');

            Detail.selectedGradeId = Number(item.dataset.gradeId);
            Detail.selectedPrice = Number(item.dataset.price);
            Detail.selectedRemain = Number(item.dataset.remain);
            Detail.quantity = 1;

            Detail.updateUI();
        });

    },

    bindQuantityControl() {
        document.getElementById('quantity-minus').addEventListener('click', () => {
            if (Detail.quantity <= 1) return;
            Detail.quantity--;
            Detail.updateUI();
        });

        document.getElementById('quantity-plus').addEventListener('click', () => {
            if (Detail.quantity >= Detail.selectedRemain) {
                alert('잔여 좌석 수를 초과할 수 없습니다.');
                return;
            }
            Detail.quantity++;
            Detail.updateUI();
        });
    },

    updateUI() {
        document.getElementById('quantity-value').textContent = Detail.quantity;
        document.getElementById('total-price').textContent =
            (Detail.selectedPrice * Detail.quantity).toLocaleString() + '원';

        const reserveBtn = document.getElementById('reserve-btn');

        if (!Detail.selectedScheduleId) {
            reserveBtn.disabled = true;
            reserveBtn.textContent = '회차를 선택해주세요';
        } else if (!Detail.selectedGradeId) {
            reserveBtn.disabled = true;
            reserveBtn.textContent = '등급을 선택해주세요';
        } else {
            reserveBtn.disabled = false;
            reserveBtn.textContent = '예매하기';
        }

    },

    reserve() {
        const performanceId = document.getElementById('btn-reserve').dataset.performanceId;

        authFetch('/api/reservations', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                performanceId: performanceId,
                scheduleId: Detail.selectedScheduleId,
                seatGradeId: Detail.selectedGradeId,
                quantity: Detail.quantity,
                deliveryType: document.getElementById('delivery-type').value,
                token: Detail.token
            })
        })
        .then(async res => {
            if (!res.ok) throw new Error((await res.json()).message || '공연 예매에 실패했습니다.');
            alert('예매가 완료되었습니다.');
            location.href = '/';
        })
        .catch(err => {
            alert(err.message);
            console.error(err);
        });
    }

}

document.addEventListener('DOMContentLoaded', () => Detail.init());

window.addEventListener('beforeunload', () => {
    if (Detail.token) {
        const performanceId = document.getElementById('performance-id').value;
        navigator.sendBeacon(
            `/api/queue/${performanceId}/leave-active?token=${Detail.token}`
        );
    }
});