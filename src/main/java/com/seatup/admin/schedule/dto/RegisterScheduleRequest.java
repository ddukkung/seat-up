package com.seatup.admin.schedule.dto;

import com.seatup.admin.seat.dto.RegisterSeatGradeRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterScheduleRequest {

    @NotNull
    private Long performanceId;

    @NotNull
    private LocalDate performanceDate;

    @NotNull
    private LocalTime performanceTime;

    @Valid
    @NotEmpty
    private List<RegisterSeatGradeRequest> seatGrades;

}
