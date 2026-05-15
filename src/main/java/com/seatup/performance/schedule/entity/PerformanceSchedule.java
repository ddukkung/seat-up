package com.seatup.performance.schedule.entity;

import com.seatup.performance.entity.Performance;
import com.seatup.seat.entity.SeatGrade;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PERFORMANCE_SCHEDULE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerformanceSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERFORMANCE_ID", nullable = false)
    private Performance performance;

    @Column(name = "PERFORMANCE_DATE", nullable = false)
    private LocalDate performanceDate;

    @Column(name = "PERFORMANCE_TIME", nullable = false)
    private LocalTime performanceTime;

    @Column(name = "ROUND", nullable = false)
    private int round;

    @OneToMany(mappedBy = "performanceSchedule", fetch = FetchType.LAZY)
    private List<SeatGrade> seatGrades = new ArrayList<>();

    @Builder
    public PerformanceSchedule(Performance performance, LocalDate performanceDate, LocalTime performanceTime, int round) {
        this.performance = performance;
        this.performanceDate = performanceDate;
        this.performanceTime = performanceTime;
        this.round = round;
    }

    public void update(Performance performance, LocalDate performanceDate, LocalTime performanceTime) {
        this.performance = performance;
        this.performanceDate = performanceDate;
        this.performanceTime = performanceTime;
    }

}
