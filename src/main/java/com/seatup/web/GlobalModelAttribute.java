package com.seatup.web;

import com.seatup.category.dto.CategoryListResponse;
import com.seatup.category.entity.Category;
import com.seatup.category.service.CategoryService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalModelAttribute {

    private final CategoryService categoryService;

    public GlobalModelAttribute(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ModelAttribute("categoryList")
    public List<CategoryListResponse> categoryList() {
        return categoryService.getCategoryList();
    }
}
