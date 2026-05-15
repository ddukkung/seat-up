"use strict";

import {authFetch} from '/js/common/authFetch.js';

const PerformanceList = {
    init() {
        const deleteButtons = document.querySelectorAll('.btn-delete');

        deleteButtons.forEach(button => {
            button.addEventListener('click', function() {
                const performanceId = this.getAttribute('data-id');

                if (confirm('정말 삭제하시겠습니까?')) {
                    PerformanceList.deletePerformance(performanceId);
                }
            });
        });
    },

    deletePerformance(id) {
        authFetch(`/api/admin/performances/${id}`, {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json'}
        })
        .then(async res => {
            if (!res.ok) throw new Error((await res.json()).message || '공연 삭제에 실패했습니다.');
            alert('공연이 삭제되었습니다.');
            location.href = '/admin/performances';
        })
        .catch(err => {
            alert(err);
            console.error(err);
        });
    }
}

document.addEventListener('DOMContentLoaded', () => PerformanceList.init());