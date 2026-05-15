"use strict";

export function validatePerformance(performance, status) {
    if (!performance.categoryId) {
        alert('카테고리를 선택해주세요.');
        document.getElementById('category').focus();
        return false;
    }

    if (!performance.title) {
        alert('공연명을 입력해주세요.');
        document.getElementById('title').focus();
        return false;
    }

    if (!performance.startDateTime) {
        alert('공연 시작일시를 지정해주세요.');
        document.getElementById('start-date-time').focus();
        return false;
    }

    if (!performance.endDateTime) {
        alert('공연 종료일시를 지정해주세요.');
        document.getElementById('end-date-time').focus();
        return false;
    }

    if (!performance.openDateTime) {
        alert('티켓 예매 오픈 일시를 지정해주세요.');
        document.getElementById('open-date-time').focus();
        return false;
    }

    if (!performance.closeDateTime) {
        alert('티켓 예매 종료 일시를 지정해주세요.');
        document.getElementById('close-date-time').focus();
        return false;
    }

    if (!performance.venue) {
        alert('공연 장소를 입력해주세요.');
        document.getElementById('venue').focus();
        return false;
    }

    return true;
}

export function validateSchedule(schedule, isEdit = false) {
    const today = new Date().toISOString().split('T')[0];

    if (!schedule.performanceDate || schedule.performanceDate.trim() === '') {
        alert('공연 일자를 선택해주세요.');
        document.getElementById('modal-performance-date').focus();
        return false;
    }

    if (!isEdit && schedule.performanceDate < today) {
        alert('신규 등록 시 공연 일자는 오늘 이후의 날짜여야 합니다.');
        document.getElementById('modal-performance-date').focus();
        return false;
    }

    if (!schedule.performanceTime || schedule.performanceTime.trim() === '') {
        alert('공연 시간을 선택해주세요.');
        document.getElementById('modal-performance-time').focus();
        return false;
    }

    return true;
}

export function validateSeatGrades(seatGrades) {
    if (!seatGrades || seatGrades.length === 0) {
        alert('1개 이상의 좌석 등급 정보를 입력해주세요.');
        return false;
    }

    if (!isDuplicateGrade(seatGrades)) {
        return false;
    }

    for (const seat of seatGrades) {
        if (!seat.grade || seat.grade.trim() === '') {
            alert('좌석의 등급을 선택해주세요.');
            return false;
        }

        if (seat.price === "" || isNaN(seat.price) || seat.price === null) {
            alert('좌석의 금액을 입력해주세요.');
            return false;
        }

        const price = Number(seat.price);
        if (price < 0) {
            alert('좌석의 금액은 0원 이상이어야 합니다.');
            return false;
        }

        if (seat.totalQuantity === "" || isNaN(seat.totalQuantity) || Number(seat.totalQuantity) <= 0) {
            alert('좌석의 수량을 1개 이상 입력해주세요.');
            return false;
        }
    }

    return true;
}

function isDuplicateGrade(seatGrades) {
    const gradeNames = seatGrades
        .map(seat => seat.grade ? seat.grade.trim() : '')
        .filter(name => name !== '');

    const duplicateCheck = new Set(gradeNames);
    if (gradeNames.length !== duplicateCheck.size) {
        alert('중복된 좌석 등급이 있습니다. 등급명은 서로 달라야 합니다.');
        return false;
    }

    return true;
}