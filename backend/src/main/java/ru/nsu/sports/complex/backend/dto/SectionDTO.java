package ru.nsu.sports.complex.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SectionDTO {

    @NotBlank(message = "Name must not be blank")
    private String name;

    private String teacher;
    private String place;
    private ScheduleDTO schedule;
}
