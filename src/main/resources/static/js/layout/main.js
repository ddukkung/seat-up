"use strict";

const Main = {
    init() {
        this.loginArea = document.querySelector('.guest');
        this.userArea = document.querySelector('.auth');

        this.renderAuthArea();
        this.bindLogout();
    },

    renderAuthArea() {
        const token = this.getToken();

        if (!token) {
            this.showGuest();
            return;
        }

        this.fetchCurrentUser();
    },

    getToken() {
        return localStorage.getItem('accessToken');
    },

    showGuest() {
        this.loginArea.classList.remove('hidden');
        this.userArea.classList.add('hidden');
    },

    showAuth(user) {
        this.loginArea.classList.add('hidden');
        this.userArea.classList.remove('hidden');

        const usernameEl = document.querySelector('.username');
        if (usernameEl) {
            usernameEl.innerText = user.name;
        }

        const adminLink = document.querySelector('.admin-link');
        if (adminLink && user.role === 'ADMIN') {
            adminLink.classList.remove('hidden');
        }
    },

    fetchCurrentUser(token) {
        fetch('/api/users/me', {
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
            }
        })
        .then(res => {
            if (!res.ok) {
                throw new Error();
            }

            return res.json();
        })
        .then(user => {
            this.showAuth(user);
        })
        .catch(() => {
            localStorage.removeItem('accessToken');
            this.showGuest();
        })
    },

    bindLogout() {
        const logoutBtn = document.getElementById('logoutBtn');
        logoutBtn.addEventListener('click', () => {
            localStorage.removeItem('accessToken');
            this.renderAuthArea();
            location.href = '/';
        });
    }

}

document.addEventListener('DOMContentLoaded', () => Main.init());