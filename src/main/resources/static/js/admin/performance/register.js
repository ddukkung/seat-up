"use strict";

import * as Validation from '/js/validation/performance-validation.js';
import {authFetch} from '/js/common/authFetch.js';
import * as Common from '/js/common/performance-common.js';

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('performance-form');
    if (!form || form.dataset.mode !== 'register') return;

    // 최초 등록 시에는 performanceId가 없으므로 회차 등록 불가능. 영역 숨김 처리
    document.querySelector('.schedule-management-section').style.display = 'none';

    Common.initPosterUpload();

    form.addEventListener('submit', e => {
        e.preventDefault();

        const performance = {
            ...Common.getBaseInputData()
        };

        if (!Validation.validatePerformance(performance, 'SCHEDULED')) return;

        const formData = new FormData();
        const performanceBlob = new Blob([JSON.stringify(performance)], { type: 'application/json' });
        formData.append('performance', performanceBlob);

        const posterFile = document.getElementById('poster-file').files[0];
        if (posterFile) {
            formData.append('posterFile', posterFile);
        }

        authFetch('/api/admin/performances', {
            method: 'POST',
            body: formData
        })
        .then(async res => {
            const performanceId = await res.json();
            if (!res.ok) throw new Error('공연 등록에 실패했습니다.');
            alert('공연을 등록하였습니다. 회차를 등록하여 주십시오.');
            location.href = `/admin/performances/${performanceId}`;
        })
        .catch(err => {
            alert(err.message);
            console.error(err);
        });
    });
});