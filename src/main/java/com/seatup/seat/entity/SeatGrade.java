package com.seatup.seat.entity;

import com.seatup.performance.schedule.entity.PerformanceSchedule;
import com.seatup.seat.enums.Grade;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SEAT_GRADE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeatGrade {

    @Version
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHEDULE_ID", nullable = false)
    private PerformanceSchedule performanceSchedule;

    @Enumerated(EnumType.STRING)
    @Column(name = "GRADE", nullable = false, length = 10)
    private Grade grade;

    @Column(name = "PRICE", nullable = false)
    private int price;

    @Column(name = "TOTAL_QUANTITY", nullable = false)
    private int totalQuantity;

    @Column(name = "REMAIN_QUANTITY", nullable = false)
    private int remainQuantity;

    @Builder
    public SeatGrade(PerformanceSchedule performanceSchedule, Grade grade, int price, int totalQuantity) {
        this.performanceSchedule = performanceSchedule;
        this.grade = grade;
        this.price = price;
        this.totalQuantity = totalQuantity;
        this.remainQuantity = totalQuantity;
    }

    public void update(Grade grade, int price, int totalQuantity, int newRemainQuantity) {
        this.grade = grade;
        this.price = price;
        this.totalQuantity = totalQuantity;
        this.remainQuantity = newRemainQuantity;
    }

    public void decreaseRemainQuantity(int quantity) {
        this.remainQuantity -= quantity;
    }

}
