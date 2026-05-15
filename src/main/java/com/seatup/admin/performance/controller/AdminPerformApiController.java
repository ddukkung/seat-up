package com.seatup.admin.performance.controller;

import com.seatup.admin.performance.service.AdminPerformService;
import com.seatup.admin.performance.dto.UpdatePerformanceRequest;
import com.seatup.admin.schedule.dto.AdminScheduleListResponse;
import com.seatup.common.file.FileService;
import com.seatup.jwt.UserPrincipal;
import com.seatup.admin.performance.dto.RegisterPerformanceRequest;
import com.seatup.performance.schedule.service.PerformanceScheduleService;
import com.seatup.user.entity.User;
import com.seatup.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/admin/performances")
public class AdminPerformApiController {

    private final AdminPerformService performService;
    private final UserService userService;
    private final FileService fileService;

    public AdminPerformApiController(AdminPerformService performService, UserService userService, FileService fileService) {
        this.performService = performService;
        this.userService = userService;
        this.fileService = fileService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> registerPerformance(@AuthenticationPrincipal UserPrincipal principal,
                                                    @RequestPart("performance") @Valid RegisterPerformanceRequest performanceRequest,
                                                    @RequestPart(value = "posterFile", required = false)MultipartFile posterFile
                                                    ) throws IOException {
        User user = userService.findById(principal.getUserId());

        String posterUrl = null;
        if (posterFile != null && !posterFile.isEmpty()) {
            posterUrl = fileService.save(posterFile);
            performanceRequest.setPosterUrl(posterUrl);
        }

        Long performanceId = performService.registerPerformance(user, performanceRequest);

        return ResponseEntity.ok(performanceId);
    }

    @PutMapping
    public ResponseEntity<Void> updatePerformance(@RequestPart("performance") @Valid UpdatePerformanceRequest updateRequest,
                                                  @RequestPart(value = "posterFile", required = false) MultipartFile posterFile) throws IOException {
        String posterUrl = null;
        if (posterFile != null && !posterFile.isEmpty()) {
            posterUrl = fileService.save(posterFile);
            updateRequest.setPosterUrl(posterUrl);
        }

        performService.updatePerformance(updateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePerformance(@PathVariable("id") Long id) {
        performService.deletePerformance(id);
        return ResponseEntity.ok("공연이 삭제되었습니다.");
    }

}
