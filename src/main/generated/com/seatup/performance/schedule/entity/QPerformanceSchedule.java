package com.seatup.performance.schedule.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPerformanceSchedule is a Querydsl query type for PerformanceSchedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPerformanceSchedule extends EntityPathBase<PerformanceSchedule> {

    private static final long serialVersionUID = 539382708L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPerformanceSchedule performanceSchedule = new QPerformanceSchedule("performanceSchedule");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.seatup.performance.entity.QPerformance performance;

    public final DatePath<java.time.LocalDate> performanceDate = createDate("performanceDate", java.time.LocalDate.class);

    public final TimePath<java.time.LocalTime> performanceTime = createTime("performanceTime", java.time.LocalTime.class);

    public final NumberPath<Integer> round = createNumber("round", Integer.class);

    public final ListPath<com.seatup.seat.entity.SeatGrade, com.seatup.seat.entity.QSeatGrade> seatGrades = this.<com.seatup.seat.entity.SeatGrade, com.seatup.seat.entity.QSeatGrade>createList("seatGrades", com.seatup.seat.entity.SeatGrade.class, com.seatup.seat.entity.QSeatGrade.class, PathInits.DIRECT2);

    public QPerformanceSchedule(String variable) {
        this(PerformanceSchedule.class, forVariable(variable), INITS);
    }

    public QPerformanceSchedule(Path<? extends PerformanceSchedule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPerformanceSchedule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPerformanceSchedule(PathMetadata metadata, PathInits inits) {
        this(PerformanceSchedule.class, metadata, inits);
    }

    public QPerformanceSchedule(Class<? extends PerformanceSchedule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.performance = inits.isInitialized("performance") ? new com.seatup.performance.entity.QPerformance(forProperty("performance"), inits.get("performance")) : null;
    }

}

