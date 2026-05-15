package com.seatup.reservation.batch;

import com.seatup.reservation.entity.Reservation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;

@Slf4j
public class ReservationItemErrorListener implements ItemProcessListener<Reservation, Reservation> {

    @Override
    public void onProcessError(Reservation item, Exception e) {
        log.error("[Batch-Error] 프로세싱 실패 - ID: {}, 사유: {}", item.getId(), e.getMessage());
    }
}
