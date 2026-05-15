package com.seatup.performance.controller;

import com.seatup.performance.service.PerformanceService;
import com.seatup.user.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/performances")
public class PerformanceApiController {

    private final PerformanceService performanceService;
    private final UserService userService;

    public PerformanceApiController(PerformanceService performanceService, UserService userService) {
        this.performanceService = performanceService;
        this.userService = userService;
    }

}
