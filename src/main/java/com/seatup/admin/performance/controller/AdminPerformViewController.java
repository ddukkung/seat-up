package com.seatup.admin.performance.controller;

import com.seatup.admin.performance.service.AdminPerformService;
import com.seatup.admin.performance.dto.AdminPerformanceResponse;
import com.seatup.common.performance.PerformanceQueryService;
import com.seatup.performance.entity.Performance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/performances")
public class AdminPerformViewController {

    private final AdminPerformService performanceService;

    public AdminPerformViewController(AdminPerformService performanceService) {
        this.performanceService = performanceService;
    }

    @GetMapping
    public String performanceList(Model model) {
        model.addAttribute("performanceList", performanceService.getPerformanceList());
        return "admin/performance/list";
    }

    @GetMapping("/register")
    public String registerPerformanceForm() {
        return "admin/performance/register";
    }

    @GetMapping("/{id}")
    public String editPerformanceForm(@PathVariable("id") Long id, Model model) {
        AdminPerformanceResponse response = performanceService.findPerformance(id);
        model.addAttribute("performance", response);
        return "admin/performance/edit";
    }
}
