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

    @NotNull(message = "Day of the week must not be null")
    private String dayOfWeek;

    @NotBlank(message = "Start time must not be blank")
    private String startTime;

    @NotBlank(message = "End time must not be blank")
    private String endTime;
}
