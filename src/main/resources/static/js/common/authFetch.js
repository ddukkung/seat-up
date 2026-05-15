export async function authFetch(url, options = {}) {
    const accessToken = localStorage.getItem('accessToken');
    const refreshToken = localStorage.getItem('refreshToken');

    // FormData면 Content-Type 헤더 건드리지 않음
    const isFormData = options.body instanceof FormData;

    const headers = isFormData
        ? {'Authorization': accessToken ? 'Bearer ' + accessToken : undefined}
        : {...(options.headers || {}), 'Authorization': accessToken ? 'Bearer ' + accessToken : undefined };

    const res = await fetch(url, {
        ...options,
        headers
    });

    // 정상 응답이면 그대로 반환
    if (res.status !== 401) {
        return res;
    }

    // access 만료 -> refresh 시도
    const refreshRes = await fetch('/api/auth/refresh', {
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + refreshToken
        }
    });

    if (!refreshRes.ok) {
        // refresh도 실패 -> 완전 로그아웃
        localStorage.removeItem('accessToken');
        location.href = '/auth/login';
        throw new Error('unauthorized');
    }

    const data = await refreshRes.json();
    const newAccess = data.accessToken;
    localStorage.setItem('accessToken', newAccess);

    // 원래 요청을 새 토큰으로 재시도
    return fetch(url, {
        ...options,
        headers: isFormData
            ? { 'Authorization': 'Bearer ' + newAccess }
            : { ...(options.headers || {}), 'Authorization': 'Bearer ' + newAccess }
    });
}