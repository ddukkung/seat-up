package com.seatup.admin.seat.dto;

import com.seatup.seat.enums.Grade;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterSeatGradeRequest implements SeatGradeDto {

    @NotNull
    private Grade grade;

    @Min(0)
    private int price;

    @Min(1)
    private int totalQuantity;

}
