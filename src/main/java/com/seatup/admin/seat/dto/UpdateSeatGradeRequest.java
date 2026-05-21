package com.seatup.admin.seat.dto;

import com.seatup.seat.enums.Grade;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSeatGradeRequest implements SeatGradeDto {

    private Long id;

    @NotNull
    private Grade grade;

    @Min(0)
    private int price;

    @Min(1)
    private int totalQuantity;
}
