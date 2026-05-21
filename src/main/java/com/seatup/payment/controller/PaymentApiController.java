package com.seatup.payment.controller;

import com.seatup.jwt.UserPrincipal;
import com.seatup.payment.dto.PaymentCancelRequest;
import com.seatup.payment.dto.PaymentReadyRequest;
import com.seatup.payment.service.PaymentService;
import com.seatup.user.entity.User;
import com.seatup.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentApiController {

    private final PaymentService paymentService;
    private final UserService userService;

    @PostMapping("/mock")
    public ResponseEntity<String> payment(@AuthenticationPrincipal UserPrincipal principal,
                                          @RequestBody PaymentReadyRequest request) {
        User user = userService.findById(principal.getUserId());
        paymentService.preparePayment(user, request);
        return ResponseEntity.ok("결제가 완료되었습니다.");
    }

    @PostMapping("/ready")
    public ResponseEntity<Void> ready(@AuthenticationPrincipal UserPrincipal principal, @RequestBody PaymentReadyRequest request) {
        User user = userService.findById(principal.getUserId());
        paymentService.preparePayment(user, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancel(@AuthenticationPrincipal UserPrincipal principal, @RequestBody PaymentCancelRequest request) {
        User user = userService.findById(principal.getUserId());
        paymentService.cancelPayment(user, request.reservationId());
        return ResponseEntity.ok().build();
    }
}
