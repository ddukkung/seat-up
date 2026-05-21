package com.seatup.payment.controller;

import com.seatup.common.exception.BusinessException;
import com.seatup.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/users/mypage/reservations/payment")
@RequiredArgsConstructor
public class PaymentViewController {

    private final PaymentService paymentService;

    @GetMapping("/success")
    public String success(@RequestParam("paymentKey") String paymentKey, @RequestParam("orderId") String orderId,
                          @RequestParam("amount") int amount, Model model) {
        try {
            paymentService.confirmPayment(paymentKey, orderId, amount);
            model.addAttribute("paymentKey", paymentKey);
            model.addAttribute("orderId", orderId);
            model.addAttribute("amount", amount);
            return "mypage/reservations/payment/success";
        } catch (BusinessException e) {
            String encodedMessage = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            return "redirect:/users/mypage/reservations/payment/fail?errorMessage=" + encodedMessage;
        }
    }

    @GetMapping("/fail")
    public String fail(@RequestParam(name = "errorMessage", required = false) String errorMessage,
                       @RequestParam(name = "reservationId", required = false) String reservationId,
                       Model model) {
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("reservationId", reservationId);
        return "mypage/reservations/payment/fail";
    }
}
