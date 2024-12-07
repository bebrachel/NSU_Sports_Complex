package ru.nsu.sports.complex.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotDTO {

    @NotNull
    private String dayOfWeek;

    @NotBlank
    private String startTime;

    @NotBlank
    private String endTime;
}