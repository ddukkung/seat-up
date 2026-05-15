package com.seatup.seat.dto;

import com.seatup.seat.entity.SeatGrade;
import com.seatup.seat.enums.Grade;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;

@Getter
@AllArgsConstructor
public class SeatGradeResponse {

    private Long id;

    private Grade grade;

    private int price;

    private int totalQuantity;

    private int remainQuantity;

    public static List<SeatGradeResponse> from(List<SeatGrade> seatGrades) {
        return seatGrades.stream()
                .sorted(Comparator.comparingInt(sg -> sg.getGrade().getOrder()))
                .map(seatGrade -> new SeatGradeResponse(
                        seatGrade.getId(),
                        seatGrade.getGrade(),
                        seatGrade.getPrice(),
                        seatGrade.getTotalQuantity(),
                        seatGrade.getRemainQuantity()
                ))
                .toList();
    }

}
