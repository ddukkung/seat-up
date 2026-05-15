package com.seatup.seat.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSeatGrade is a Querydsl query type for SeatGrade
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSeatGrade extends EntityPathBase<SeatGrade> {

    private static final long serialVersionUID = -1712116113L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSeatGrade seatGrade = new QSeatGrade("seatGrade");

    public final EnumPath<com.seatup.seat.enums.Grade> grade = createEnum("grade", com.seatup.seat.enums.Grade.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.seatup.performance.schedule.entity.QPerformanceSchedule performanceSchedule;

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final NumberPath<Integer> remainQuantity = createNumber("remainQuantity", Integer.class);

    public final NumberPath<Integer> totalQuantity = createNumber("totalQuantity", Integer.class);

    public final NumberPath<Long> version = createNumber("version", Long.class);

    public QSeatGrade(String variable) {
        this(SeatGrade.class, forVariable(variable), INITS);
    }

    public QSeatGrade(Path<? extends SeatGrade> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSeatGrade(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSeatGrade(PathMetadata metadata, PathInits inits) {
        this(SeatGrade.class, metadata, inits);
    }

    public QSeatGrade(Class<? extends SeatGrade> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.performanceSchedule = inits.isInitialized("performanceSchedule") ? new com.seatup.performance.schedule.entity.QPerformanceSchedule(forProperty("performanceSchedule"), inits.get("performanceSchedule")) : null;
    }

}

