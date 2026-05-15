"use strict";

export function getBaseInputData() {
    return {
        categoryId: Number(document.getElementById('category').value),
        title: document.getElementById('title').value.trim(),
        description: document.getElementById('description').value.trim(),
        startDateTime: document.getElementById('start-date-time').value.trim(),
        endDateTime: document.getElementById('end-date-time').value.trim(),
        openDateTime: document.getElementById('open-date-time').value.trim(),
        closeDateTime: document.getElementById('close-date-time').value.trim(),
        venue: document.getElementById('venue').value.trim(),
    };
}

export function initPosterUpload() {
    const posterArea = document.getElementById('poster-upload-area');
    const posterFile = document.getElementById('poster-file');
    const posterPlaceholder = document.getElementById('poster-placeholder');

    posterArea.addEventListener('click', () => posterFile.click());

    posterArea.addEventListener('dragover', e => {
        e.preventDefault();
        posterArea.classList.add('drag-over');
    });

    posterArea.addEventListener('dragleave', () => {
        posterArea.classList.remove('drag-over');
    });

    posterArea.addEventListener('drop', e => {
        e.preventDefault();
        posterArea.classList.remove('drag-over');
        const file = e.dataTransfer.files[0];
        if (file) showPreview(file, posterArea, posterPlaceholder);
    });

    posterFile.addEventListener('change', () => {
        if (posterFile.files[0]) {
            showPreview(posterFile.files[0], posterArea, posterPlaceholder);
        }
    });
}

function showPreview(file, posterArea, posterPlaceholder) {
    const reader = new FileReader();
    reader.onload = e => {
        let img = document.getElementById('poster-preview');
        if (!img) {
            img = document.createElement('img');
            img.id = 'poster-preview';
            img.className = 'poster-preview-img';
            posterArea.prepend(img);
        }

        img.src = e.target.result;
        if (posterPlaceholder) posterPlaceholder.classList.add('hidden');
    };

    reader.readAsDataURL(file);
}