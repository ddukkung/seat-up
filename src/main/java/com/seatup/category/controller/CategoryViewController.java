package com.seatup.category.controller;

import com.seatup.category.entity.Category;
import com.seatup.category.service.CategoryService;
import com.seatup.performance.service.PerformanceService;
import com.seatup.performance.dto.PerformanceListResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryViewController {

    private final CategoryService categoryService;
    private final PerformanceService performanceService;

    public CategoryViewController(CategoryService categoryService, PerformanceService performanceService) {
        this.categoryService = categoryService;
        this.performanceService = performanceService;
    }

    @GetMapping("/{categoryId}")
    public String performancesByCategory(@PathVariable("categoryId") Long categoryId, Model model) {
        List<PerformanceListResponse> performances = performanceService.findPerformancesByCategory(categoryId);
        Category category = categoryService.findById(categoryId);
        model.addAttribute("performances", performances);
        model.addAttribute("title", category.getCategoryName() + " 공연 목록");
        return "performance/list";
    }
}
