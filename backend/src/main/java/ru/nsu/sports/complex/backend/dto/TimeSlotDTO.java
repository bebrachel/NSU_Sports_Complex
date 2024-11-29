package ru.nsu.sports.complex.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotDTO {
    private String dayOfWeek;
    private String startTime;
    private String endTime;
}
