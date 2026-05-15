package com.seatup.reservation.controller;

import com.seatup.common.exception.BusinessException;
import com.seatup.jwt.UserPrincipal;
import com.seatup.reservation.service.ReservationService;
import com.seatup.reservation.dto.ReservationRequest;
import com.seatup.user.entity.User;
import com.seatup.user.service.UserService;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
public class ReservationApiController {

    private final UserService userService;
    private final ReservationService reservationService;

    public ReservationApiController(UserService userService, ReservationService reservationService) {
        this.userService = userService;
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<Void> reserve(@AuthenticationPrincipal UserPrincipal principal,
                                        @RequestBody @Valid ReservationRequest request) {

        User user = userService.findById(principal.getUserId());

        try {
            reservationService.reserve(user, request);
        } catch (OptimisticLockException e) {
            throw new BusinessException("예매에 실패했습니다. 다시 시도해주세요.");
        }

        return ResponseEntity.ok().build();
    }

}
