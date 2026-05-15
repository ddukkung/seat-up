package com.seatup.user.mypage.dto;

import com.seatup.common.util.ReservationMapper;
import com.seatup.payment.entity.Payment;
import com.seatup.performance.entity.Performance;
import com.seatup.performance.schedule.entity.PerformanceSchedule;
import com.seatup.reservation.entity.Reservation;
import com.seatup.reservation.enums.ReservationStatus;
import com.seatup.seat.entity.SeatGrade;
import com.seatup.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReservationDetailResponse {

    private UserInfo user;

    private PerformanceInfo performance;

    private ScheduleInfo schedule;

    private SeatInfo seatGrade;

    private ReservationInfo reservation;

    private PaymentInfo payment;

    private boolean cancelable;

    public static ReservationDetailResponse from(User user, Reservation res, Payment pay) {
        SeatGrade seat = res.getSeatGrade();

        LocalDateTime deadline = res.getSchedule().getPerformanceDate()
                .minusDays(1)
                .atTime(23, 59);
        boolean cancelable = res.getStatus() == ReservationStatus.CONFIRMED
                && LocalDateTime.now().isBefore(deadline);

        return ReservationDetailResponse.builder()
                .user(UserInfo.builder().name(user.getName()).build())
                .performance(PerformanceInfo.from(res.getPerformance()))
                .schedule(ScheduleInfo.from(res.getSchedule()))
                .seatGrade(SeatInfo.builder().seatGradeName(seat.getGrade().name()).price(seat.getPrice()).build())
                .reservation(ReservationInfo.from(res))
                .payment(pay != null ? PaymentInfo.from(pay) : null)
                .cancelable(cancelable)
                .build();
    }

    @Getter
    @Builder
    public static class UserInfo {
        private String name;
    }

    @Getter
    @Builder
    public static class PerformanceInfo {
        private String performanceTitle;
        private String posterUrl;
        private String formattedStartDateTime;
        private String venue;

        public static PerformanceInfo from(Performance per) {
            return PerformanceInfo.builder()
                    .performanceTitle(per.getTitle())
                    .posterUrl(per.getPosterUrl())
                    .formattedStartDateTime(per.getStartDateTime().format(ReservationMapper.DATE_WITH_DAY))
                    .venue(per.getVenue())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ScheduleInfo {
        private int round;
        private String formattedPerformanceDate;
        private String formattedPerformanceTime;

        public static ScheduleInfo from(PerformanceSchedule schedule) {
            return ScheduleInfo.builder()
                    .round(schedule.getRound())
                    .formattedPerformanceDate(schedule.getPerformanceDate().format(ReservationMapper.DATE))
                    .formattedPerformanceTime(schedule.getPerformanceTime().format(ReservationMapper.TIME))
                    .build();
        }
    }

    @Getter
    @Builder
    public static class SeatInfo {
        private String seatGradeName;
        private int price;
    }

    @Getter
    @Builder
    public static class ReservationInfo {
        private Long id;
        private String deliveryType;
        private ReservationStatus reservationStatus;
        private String statusClass;
        private String formattedReservedAt;
        private int unitPrice;
        private int quantity;
        private int totalPrice;

        public static ReservationInfo from(Reservation res) {
            return ReservationInfo.builder()
                    .id(res.getId())
                    .deliveryType(res.getDeliveryType().getLabel())
                    .reservationStatus(res.getStatus())
                    .statusClass(ReservationMapper.getStatusClass(res.getStatus()))
                    .formattedReservedAt(res.getReservedAt().format(ReservationMapper.DATE_WITH_DAY))
                    .unitPrice(res.getUnitPrice())
                    .quantity(res.getQuantity())
                    .totalPrice(res.getTotalPrice())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class PaymentInfo {
        private int amount;
        private String paymentStatus;
        private String paymentType;

        public static PaymentInfo from(Payment pay) {
            return PaymentInfo.builder()
                    .amount(pay.getAmount())
                    .paymentStatus(pay.getStatus().getLabel())
                    .paymentType(pay.getPaymentType().getLabel())
                    .build();
        }
    }

}
