package com.seatup.admin.seat.dto;

import com.seatup.seat.entity.SeatGrade;
import com.seatup.seat.enums.Grade;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AdminSeatGradeResponse {

    private Long id;

    private Grade grade;

    private int price;

    private int totalQuantity;

    public static List<AdminSeatGradeResponse> from(List<SeatGrade> seatGrades) {
        return seatGrades.stream()
                .map(seatGrade -> new AdminSeatGradeResponse(
                        seatGrade.getId(),
                        seatGrade.getGrade(),
                        seatGrade.getPrice(),
                        seatGrade.getTotalQuantity()
                ))
                .toList();
    }

}
