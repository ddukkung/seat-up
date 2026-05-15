package com.seatup.performance.controller;

import com.seatup.common.performance.PerformanceQueryService;
import com.seatup.performance.service.PerformanceService;
import com.seatup.performance.dto.PerformanceDetailResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/performances")
public class PerformanceViewController {

    private final PerformanceService performanceService;
    private final PerformanceQueryService performanceQueryService;

    public PerformanceViewController(PerformanceService performanceService, PerformanceQueryService performanceQueryService) {
        this.performanceService = performanceService;
        this.performanceQueryService = performanceQueryService;
    }

    @GetMapping("/{id}")
    public String performanceDetail(@PathVariable("id") Long id, Model model) {
        PerformanceDetailResponse response = performanceService.findById(id);
        model.addAttribute("performance", response);
        return "performance/detail";
    }
}
