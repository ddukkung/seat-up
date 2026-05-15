package com.seatup.admin.performance.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RegisterPerformanceRequest {

    @NotNull
    private Long categoryId;

    @NotBlank
    @Size(max = 200)
    private String title;

    @Size(max = 2000)
    private String description;

    @NotNull
    private LocalDateTime startDateTime;

    @NotNull
    private LocalDateTime endDateTime;

    @NotNull
    private LocalDateTime closeDateTime;

    @NotNull
    private LocalDateTime openDateTime;

    @NotBlank
    @Size(max = 100)
    private String venue;

    private String posterUrl;

}
