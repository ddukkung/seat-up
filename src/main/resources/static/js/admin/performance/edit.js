"use strict";

import * as Validation from '/js/validation/performance-validation.js';
import {authFetch} from '/js/common/authFetch.js';
import * as Common from '/js/common/performance-common.js';

const Edit = {
    currentScheduleId: null,
    performanceStatus: null,
    init() {
        const form = document.getElementById('performance-form');
        if (!form || form.dataset.mode !== 'edit') return;

        Edit.performanceStatus = document.getElementById('performance-status').value;

        this.initFormEvents();
        this.initSubmitButton();
        this.initScheduleButtons();
        this.initModalEvents();
        this.loadSchedules();
    },

    initFormEvents() {
        document.getElementById('performance-form')
            .addEventListener('submit', (e) => this.updatePerformance(e));
    },

    initSubmitButton() {
        const allowed = ['DRAFT', 'SCHEDULED'];
        document.getElementById('btn-submit')
            .classList.toggle('hidden', !allowed.includes(Edit.performanceStatus));
    },

    initScheduleButtons() {
        const scheduleAddBtn = document.getElementById('schedule-add-btn');
        if (Edit.performanceStatus === 'CLOSED') {
            scheduleAddBtn.classList.add('hidden');
        } else {
            scheduleAddBtn.addEventListener('click', () => Edit.openRegisterModal());
        }
    },

    initModalEvents() {
        document.getElementById('schedule-modal-close').addEventListener('click', () => Edit.closeModal());
        document.getElementById('modal-add-grade-btn').addEventListener('click', () => Edit.addSeatRow());
        document.getElementById('save-schedule-btn')
            .addEventListener('click', (e) => {
                e.preventDefault();
                Edit.saveSchedule();
            });

        document.getElementById('modal-seat-grade-list')
            ?.addEventListener('click', (e) => {
                if (e.target.classList.contains('grade-remove-btn')) {
                    e.target.closest('.modal-grade-item-admin').remove();
                }
            });
    },

    async loadSchedules() {
        const performanceId = document.getElementById('performance-id').value;

        try {
            const res = await authFetch(`/api/admin/schedules?performanceId=${performanceId}`);
            if (!res.ok) throw new Error('조회 실패');

            const data = await res.json();
            this.renderSchedules(data.schedules || []);

        } catch (err) {
            alert("회차 목록을 불러오는 중 오류가 발생했습니다.");
            console.error(err);
        }
    },

    renderSchedules(schedules) {
        const performanceId = document.getElementById('performance-id').value;
        const listBody = document.getElementById('schedule-list-body');
        const template = document.getElementById('schedule-row-template');
        const status = Edit.performanceStatus;

        listBody.innerHTML = ''; // 로딩 메시지 삭제

        if (schedules.length === 0) {
            listBody.innerHTML = '<tr><td colspan="4" class="empty-msg">등록된 회차 정보가 없습니다.</td></tr>';
            return;
        }

        schedules.forEach(s => {
            const clone = template.content.cloneNode(true);

            clone.querySelector('.col-round').textContent = s.round;
            clone.querySelector('.col-date').textContent = s.performanceDate;
            clone.querySelector('.col-time').textContent = s.performanceTime;

            // 관리 버튼 클릭 이벤트
            const manageBtn = clone.querySelector('.btn-row-manage');
            const deleteBtn = clone.querySelector('.btn-row-delete');

            if (status === 'CLOSED') {
                manageBtn.remove();
                deleteBtn.remove();
            } else if (status === 'OPEN') {
                manageBtn.addEventListener('click', () => this.openEditModal(s.id));
                deleteBtn.remove();
            } else {
                manageBtn.addEventListener('click', () => this.openEditModal(s.id));
                deleteBtn.addEventListener('click', () => this.deleteSchedule(s.id));
            }

            listBody.appendChild(clone);
        });
    },

    openRegisterModal() {
        this.currentScheduleId = null;
        this.clearModalFields();
        this.applyPerformanceDateRange();
        document.getElementById('modal-title').innerText = '회차 등록';
        document.getElementById('save-schedule-btn').innerText = '등록';
        document.getElementById('schedule-manage-modal').classList.remove('hidden');
    },

    async openEditModal(scheduleId) {
        this.currentScheduleId = scheduleId;
        try {
            const res = await authFetch(`/api/admin/schedules/${scheduleId}`);
            if (!res.ok) throw new Error('조회 실패');

            const data = await res.json();
            this.applyPerformanceDateRange();
            this.renderScheduleDetail(data || []);
            document.getElementById('modal-title').innerText = '회차 관리';
            document.getElementById('save-schedule-btn').innerText = '수정';
            document.getElementById('schedule-manage-modal').classList.remove('hidden');

        } catch (err) {
            alert("회차 정보를 불러오는 중 오류가 발생했습니다.");
            console.error(err);
        }
    },

    clearModalFields() {
        document.getElementById('modal-performance-date').value = '';
        document.getElementById('modal-performance-time').value = '';

        const listContainer = document.getElementById('modal-seat-grade-list');
        listContainer.innerHTML = '';
    },

    applyPerformanceDateRange() {
        const dateInput = document.getElementById('modal-performance-date');
        dateInput.min = document.getElementById('start-date-time').value.split('T')[0];
        dateInput.max = document.getElementById('end-date-time').value.split('T')[0];
    },

    renderScheduleDetail(data) {
        const isScheduled = Edit.performanceStatus === 'SCHEDULED';

        const dateInput = document.getElementById('modal-performance-date');
        const timeInput = document.getElementById('modal-performance-time');

        dateInput.value = data.performanceDate;
        timeInput.value = data.performanceTime;

        dateInput.readOnly = !isScheduled;
        timeInput.readOnly = !isScheduled;

        document.getElementById('save-schedule-btn').classList.toggle('hidden', !isScheduled);
        document.getElementById('modal-add-grade-btn').classList.toggle('hidden', !isScheduled);

        const listContainer = document.getElementById('modal-seat-grade-list');
        const template = document.getElementById('modal-seat-grade-template');

        listContainer.innerHTML = '';

        data.seatGrades.forEach(seat => {
            const clone = template.content.cloneNode(true);
            const row = clone.querySelector('.seat-grade-row');

            row.dataset.id = seat.id;
            row.querySelector('.grade-select').value = seat.grade;
            row.querySelector('.grade-select').disabled = !isScheduled;
            row.querySelector('.price').value = seat.price;
            row.querySelector('.price').readOnly = !isScheduled;
            row.querySelector('.quantity').value = seat.totalQuantity;
            row.querySelector('.quantity').readOnly = !isScheduled;

            const removeBtn = row.querySelector('.grade-remove-btn');
            if (isScheduled) {
                removeBtn.addEventListener('click', () => row.remove());
            } else {
                removeBtn.remove();
            }

            listContainer.appendChild(clone);
        });
    },

    closeModal() {
        document.getElementById('schedule-manage-modal').classList.add('hidden');
    },

    addSeatRow() {
        const template = document.getElementById('modal-seat-grade-template');
        const list = document.getElementById('modal-seat-grade-list');

        if (!template || !list) return;

        const clone = template.content.cloneNode(true);
        list.appendChild(clone);
    },

    saveSchedule() {
        const isUpdate = !!this.currentScheduleId;
        const url = isUpdate ? `/api/admin/schedules/${this.currentScheduleId}` : '/api/admin/schedules';
        const method = isUpdate ? 'PUT' : 'POST';

        const scheduleData = {
            performanceId: document.getElementById('performance-id').value,
            performanceDate: document.getElementById('modal-performance-date').value.trim(),
            performanceTime: document.getElementById('modal-performance-time').value.trim()
        };

        const seatGrades = this.getSeatGradesData();

        if (!Validation.validateSchedule(scheduleData) || !Validation.validateSeatGrades(seatGrades)) {
            return;
        }

        authFetch(url, {
            method: method,
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                ...scheduleData,
                seatGrades: seatGrades
            })
        })
        .then(async res => {
            if (!res.ok) throw new Error((await res.json()).message || '회차 등록에 실패하였습니다.');
            alert(isUpdate ? '회차가 수정되었습니다.' : '회차가 등록되었습니다.');
            const data = await res.text();
            Edit.performanceStatus = data; // 상태 업데이트
            this.closeModal();
            this.loadSchedules();
        })
        .catch(err => {
            alert(err.message);
            console.error(err);
        });
    },

    getSeatGradesData() {
        return [...document.querySelectorAll('.seat-grade-row')].map(row => ({
            id: row.dataset.id ? Number(row.dataset.id) : null,
            grade: row.querySelector('select').value,
            price: Number(row.querySelector('.price').value),
            totalQuantity: Number(row.querySelector('.quantity').value)
        }));
    },

    async deleteSchedule(scheduleId) {
        if (!confirm('정말로 이 회차를 삭제하시겠습니까?')) return;

        try {
            const res = await authFetch(`/api/admin/schedules/${scheduleId}`, {
                method: 'DELETE'
            });

            if (!res.ok) throw new Error('삭제에 실패했습니다.');

            alert('삭제되었습니다.');
            this.loadSchedules();
        } catch(err) {
            alert(err.message);
            console.log(err);
        }
    },

    updatePerformance(e) {
        e.preventDefault();

        const performance = {
            ...Common.getBaseInputData(),
            id: Number(document.getElementById('performance-id').value)
        };

        if (!Validation.validatePerformance(performance, status)) return;

        const formData = new FormData();
        const performanceBlob = new Blob([JSON.stringify(performance)], { type: 'application/json' });
        formData.append('performance', performanceBlob);

        const posterFile = document.getElementById('poster-file').files[0];
        if (posterFile) {
            formData.append('posterFile', posterFile);
        }

        authFetch('/api/admin/performances', {
            method: 'PUT',
            body: formData
        })
        .then(async res => {
            if (!res.ok) throw new Error((await res.json()).message || '공연 수정에 실패했습니다.');
            alert('공연을 수정하였습니다.');
            location.href = '/admin/performances';
        })
        .catch(err => {
            alert(err.message);
            console.error(err);
        })
    }
}

document.addEventListener('DOMContentLoaded', () => Edit.init());