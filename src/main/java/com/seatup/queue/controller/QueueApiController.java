package com.seatup.queue.controller;

import com.seatup.jwt.UserPrincipal;
import com.seatup.queue.dto.QueueTokenResponse;
import com.seatup.queue.service.QueueService;
import com.seatup.user.entity.User;
import com.seatup.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/queue")
public class QueueApiController {

    private final QueueService queueService;
    private final UserService userService;

    @PostMapping("/{performanceId}/enter")
    public ResponseEntity<QueueTokenResponse> enter(@PathVariable("performanceId") Long performanceId,
                                                    @AuthenticationPrincipal UserPrincipal principal) {
        User user = userService.findById(principal.getUserId());
        QueueTokenResponse response = queueService.enterQueue(performanceId, user.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{performanceId}/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable("performanceId") Long performanceId, @RequestParam String token) {
        return queueService.subscribe(performanceId, token);
    }

    @DeleteMapping("/{performanceId}/leave")
    public ResponseEntity<Void> leave(@PathVariable("performanceId") Long performanceId, @RequestParam String token) {
        queueService.removeFromQueue(performanceId, token);
        return ResponseEntity.ok().build();
    }

}
