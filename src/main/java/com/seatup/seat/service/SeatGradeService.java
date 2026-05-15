package com.seatup.seat.service;

import com.seatup.seat.repository.SeatGradeRepository;
import org.springframework.stereotype.Service;


@Service
public class SeatGradeService {

    private final SeatGradeRepository seatGradeRepository;

    public SeatGradeService(SeatGradeRepository seatGradeRepository) {
        this.seatGradeRepository = seatGradeRepository;
    }

}
