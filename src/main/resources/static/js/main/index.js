"use strict";

const Index = {
    init() {
        // 로그아웃 클릭이벤트
        document.getElementById('logout-btn').addEventListener('click', function () {
            Index.logout();
        });

        // 카테고리 클릭이벤트
        document.getElementById('category-list').addEventListener('click', (e) => {
            const target = e.target.closest('.category-item');
            if (!target) return;

            const categoryId = target.dataset.id;
            Index.moveToCategory(categoryId);
        });
    },

    logout() {
        fetch('/api/auth/logout', {
            method: 'POST'
        }).then(() => {
            location.href = '/main';
        });
    },

    moveToCategory(categoryId) {
        location.href = `/categories/${categoryId}`;
    }
}

document.addEventListener('DOMContentLoaded', () => Index.init());