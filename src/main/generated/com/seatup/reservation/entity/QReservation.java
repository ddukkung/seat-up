package com.seatup.reservation.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReservation is a Querydsl query type for Reservation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReservation extends EntityPathBase<Reservation> {

    private static final long serialVersionUID = -1803146972L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReservation reservation = new QReservation("reservation");

    public final EnumPath<com.seatup.reservation.enums.DeliveryType> deliveryType = createEnum("deliveryType", com.seatup.reservation.enums.DeliveryType.class);

    public final DateTimePath<java.time.LocalDateTime> expiredAt = createDateTime("expiredAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.seatup.performance.entity.QPerformance performance;

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> reservedAt = createDateTime("reservedAt", java.time.LocalDateTime.class);

    public final com.seatup.performance.schedule.entity.QPerformanceSchedule schedule;

    public final com.seatup.seat.entity.QSeatGrade seatGrade;

    public final EnumPath<com.seatup.reservation.enums.ReservationStatus> status = createEnum("status", com.seatup.reservation.enums.ReservationStatus.class);

    public final NumberPath<Integer> totalPrice = createNumber("totalPrice", Integer.class);

    public final NumberPath<Integer> unitPrice = createNumber("unitPrice", Integer.class);

    public final com.seatup.user.entity.QUser user;

    public QReservation(String variable) {
        this(Reservation.class, forVariable(variable), INITS);
    }

    public QReservation(Path<? extends Reservation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReservation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReservation(PathMetadata metadata, PathInits inits) {
        this(Reservation.class, metadata, inits);
    }

    public QReservation(Class<? extends Reservation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.performance = inits.isInitialized("performance") ? new com.seatup.performance.entity.QPerformance(forProperty("performance"), inits.get("performance")) : null;
        this.schedule = inits.isInitialized("schedule") ? new com.seatup.performance.schedule.entity.QPerformanceSchedule(forProperty("schedule"), inits.get("schedule")) : null;
        this.seatGrade = inits.isInitialized("seatGrade") ? new com.seatup.seat.entity.QSeatGrade(forProperty("seatGrade"), inits.get("seatGrade")) : null;
        this.user = inits.isInitialized("user") ? new com.seatup.user.entity.QUser(forProperty("user")) : null;
    }

}

