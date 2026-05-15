package com.seatup.category.exception;

import com.seatup.common.exception.BusinessException;

public class CategoryNotFoundException extends BusinessException {
    public CategoryNotFoundException() {
        super("해당 카테고리를 찾을 수 없습니다.");
    }
}
