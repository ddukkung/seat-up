package com.seatup.payment.service;

import com.seatup.common.exception.BusinessException;
import com.seatup.payment.dto.PaymentReadyRequest;
import com.seatup.payment.entity.Payment;
import com.seatup.payment.enums.PaymentStatus;
import com.seatup.payment.enums.PaymentType;
import com.seatup.payment.repository.PaymentRepository;
import com.seatup.reservation.entity.Reservation;
import com.seatup.reservation.repository.ReservationRepository;
import com.seatup.reservation.enums.ReservationStatus;
import com.seatup.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final RestTemplate restTemplate;

    @Value("${toss.secret-key}")
    private String secretKey;

    @Transactional
    public void preparePayment(User user, PaymentReadyRequest request) {
        Reservation reservation = reservationRepository.findForPayment(request.getReservationId(), user.getId())
                .orElseThrow(() -> new BusinessException("예매 내역이 존재하지 않습니다."));

        validateReservation(reservation, request.getAmount());

        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new BusinessException("본인의 예매 내역만 결제할 수 있습니다.");
        }

        Payment payment = Payment.builder()
                .reservation(reservation)
                .paymentType(PaymentType.CARD)
                .amount(request.getAmount())
                .orderId(request.getOrderId())
                .build();

        paymentRepository.save(payment);
    }

    @Transactional
    public void confirmPayment(String paymentKey, String orderId, int amount) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new BusinessException("결제 정보를 찾을 수 없습니다."));

        // 금액 위변조 검증
        if (payment.getAmount() != amount) {
            payment.fail("금액 불일치 위변조 의심");
            throw new BusinessException("결제 요청 금액이 일치하지 않습니다.");
        }

        // 토스 승인 API 호출
        callTossConfirmApi(paymentKey, orderId, amount);

        payment.complete(paymentKey);
        payment.getReservation().confirm();
    }

    private void callTossConfirmApi(String paymentKey, String orderId, int amount) {
        String url = "https://api.tosspayments.com/v1/payments/confirm";
        String encoded = Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encoded);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "paymentKey", paymentKey,
                "orderId", orderId,
                "amount", amount
        );

        try {
            restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body, headers), String.class);
        } catch (HttpClientErrorException e) {
            throw new BusinessException("토스 결제 승인에 실패했습니다: " + e.getMessage());
        }
    }

    private void validateReservation(Reservation reservation, int amount) {
        if (reservation.getStatus().equals(ReservationStatus.CONFIRMED)) {
            throw new BusinessException("이미 결제 완료된 예매 내역입니다.");
        }
        if (reservation.getStatus().equals(ReservationStatus.EXPIRED)) {
            throw new BusinessException("만료된 예매 내역입니다.");
        }
        if (reservation.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException("결제 가능 시간이 초과되었습니다.");
        }
        if (amount != reservation.getTotalPrice()) {
            throw new BusinessException("결제 요청 금액이 일치하지 않습니다.");
        }
    }

    @Transactional
    public void cancelPayment(User user, Long reservationId) {
        Reservation reservation = reservationRepository.findForPayment(reservationId, user.getId())
                .orElseThrow(() -> new BusinessException("예매 내역이 존재하지 않습니다."));

        Payment payment = paymentRepository.findByReservationIdAndStatus(reservationId, PaymentStatus.SUCCESS)
                .orElseThrow(() -> new BusinessException("결제 정보를 찾을 수 없습니다."));

        // 취소 가능 시간 검증 (공연일 하루 전 23:59까지 가능)
        LocalDateTime deadLine = reservation.getSchedule().getPerformanceDate()
                .minusDays(1)
                .atTime(23, 59);

        if (LocalDateTime.now().isAfter(deadLine)) {
            throw new BusinessException("취소 가능 기간이 지났습니다.");
        }

        // 토스 취소 API 호출
        callTossCancelApi(payment.getPaymentKey());

        payment.cancel();
        reservation.cancel();
    }

    private void callTossCancelApi(String paymentKey) {
        String url = "https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel";
        String encoded = Base64.getEncoder().encodeToString((secretKey + ":").getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encoded);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = Map.of("cancelReason", "고객 요청");

        try {
            restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body, headers), String.class);
        } catch (HttpClientErrorException e) {
            throw new BusinessException("결제 취소에 실패했습니다 : " + e.getMessage());
        }
    }

}
