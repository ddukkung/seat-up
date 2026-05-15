package com.seatup.performance.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPerformance is a Querydsl query type for Performance
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPerformance extends EntityPathBase<Performance> {

    private static final long serialVersionUID = -42834268L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPerformance performance = new QPerformance("performance");

    public final com.seatup.category.entity.QCategory category;

    public final DateTimePath<java.time.LocalDateTime> closeDateTime = createDateTime("closeDateTime", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> createdBy = createNumber("createdBy", Long.class);

    public final StringPath description = createString("description");

    public final DateTimePath<java.time.LocalDateTime> endDateTime = createDateTime("endDateTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.seatup.performance.enums.PerformanceDeleteStatus> isDeleted = createEnum("isDeleted", com.seatup.performance.enums.PerformanceDeleteStatus.class);

    public final DateTimePath<java.time.LocalDateTime> openDateTime = createDateTime("openDateTime", java.time.LocalDateTime.class);

    public final StringPath posterUrl = createString("posterUrl");

    public final ListPath<com.seatup.performance.schedule.entity.PerformanceSchedule, com.seatup.performance.schedule.entity.QPerformanceSchedule> schedules = this.<com.seatup.performance.schedule.entity.PerformanceSchedule, com.seatup.performance.schedule.entity.QPerformanceSchedule>createList("schedules", com.seatup.performance.schedule.entity.PerformanceSchedule.class, com.seatup.performance.schedule.entity.QPerformanceSchedule.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> startDateTime = createDateTime("startDateTime", java.time.LocalDateTime.class);

    public final EnumPath<com.seatup.performance.enums.PerformanceStatus> status = createEnum("status", com.seatup.performance.enums.PerformanceStatus.class);

    public final StringPath title = createString("title");

    public final StringPath venue = createString("venue");

    public QPerformance(String variable) {
        this(Performance.class, forVariable(variable), INITS);
    }

    public QPerformance(Path<? extends Performance> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPerformance(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPerformance(PathMetadata metadata, PathInits inits) {
        this(Performance.class, metadata, inits);
    }

    public QPerformance(Class<? extends Performance> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new com.seatup.category.entity.QCategory(forProperty("category")) : null;
    }

}

