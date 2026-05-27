package com.seatup.performance.entity;

import com.seatup.category.entity.Category;
import com.seatup.common.exception.BusinessException;
import com.seatup.performance.enums.PerformanceDeleteStatus;
import com.seatup.performance.enums.PerformanceStatus;
import com.seatup.performance.schedule.entity.PerformanceSchedule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "PERFORMANCE",
    indexes = {
        @Index(name = "idx_performance_status", columnList = "STATUS"),
        @Index(name = "idx_performance_start", columnList = "START_DATE_TIME")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "START_DATE_TIME", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "END_DATE_TIME", nullable = false)
    private LocalDateTime endDateTime;

    @Column(name = "OPEN_DATE_TIME", nullable = false)
    private LocalDateTime openDateTime;

    @Column(name = "CLOSE_DATE_TIME", nullable = false)
    private LocalDateTime closeDateTime;

    @Column(nullable = false, length = 100)
    private String venue;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private PerformanceStatus status;

    @Column(name = "IS_DELETED", nullable = false, length = 1)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Enumerated(EnumType.STRING)
    private PerformanceDeleteStatus isDeleted;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "CREATED_BY", nullable = false)
    private Long createdBy;

    @Column(name = "POSTER_URL", nullable = true)
    private String posterUrl;

    @OneToMany(mappedBy = "performance", fetch = FetchType.LAZY)
    private List<PerformanceSchedule> schedules;

    @Builder
    public Performance(Category category, String title, String description, LocalDateTime startDateTime,
                         LocalDateTime endDateTime, LocalDateTime openDateTime, LocalDateTime closeDateTime,
                         String venue, Long createdBy, String posterUrl) {
        this.category = category;
        this.title = title;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.openDateTime = openDateTime;
        this.closeDateTime = closeDateTime;
        this.venue = venue;
        this.status = PerformanceStatus.DRAFT;
        this.isDeleted = PerformanceDeleteStatus.N;
        this.createdAt = LocalDateTime.now();
        this.createdBy = createdBy;
        this.posterUrl = posterUrl;
    }

    public void update(Category category, String title, String description, LocalDateTime startDateTime,
                              LocalDateTime endDateTime, LocalDateTime openDateTime, LocalDateTime closeDateTime, String venue, String posterUrl
    ) {
        this.category = category;
        this.title = title;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.openDateTime = openDateTime;
        this.closeDateTime = closeDateTime;
        this.venue = venue;
        this.posterUrl = posterUrl;
    }

    public boolean isOpen() {
        return this.status == PerformanceStatus.OPEN;
    }

    public void open() {
        this.status = PerformanceStatus.OPEN;
    }

    public void close() {
        this.status = PerformanceStatus.CLOSED;
    }

    public void delete() {
        this.isDeleted = PerformanceDeleteStatus.Y;
        this.status = PerformanceStatus.CLOSED;
    }

    public void schedule() {
        if (this.status != PerformanceStatus.DRAFT) {
            throw new BusinessException("임시저장 상태일 때만 SCHEDULE로 상태 변경이 가능합니다.");
        }
        this.status = PerformanceStatus.SCHEDULED;
    }
}
