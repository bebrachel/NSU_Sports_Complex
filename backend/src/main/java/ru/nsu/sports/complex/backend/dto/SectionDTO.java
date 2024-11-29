package ru.nsu.sports.complex.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SectionDTO {
    private String name;
    private String teacher;
    private String place;
    private ScheduleDTO schedule;
}
