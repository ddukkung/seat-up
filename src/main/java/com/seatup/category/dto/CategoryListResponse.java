package com.seatup.category.dto;

import com.seatup.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryListResponse {
    private Long id;
    private String categoryName;

    public static CategoryListResponse from(Category category) {
        return new CategoryListResponse(
                category.getId(),
                category.getCategoryName()
        );
    }
}
